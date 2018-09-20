package org.taktik.freehealth.middleware.service.impl

import be.cin.mycarenet.esb.common.v2.CareProviderType
import be.cin.mycarenet.esb.common.v2.CommonInput
import be.cin.mycarenet.esb.common.v2.IdType
import be.cin.mycarenet.esb.common.v2.LicenseType
import be.cin.mycarenet.esb.common.v2.NihiiType
import be.cin.mycarenet.esb.common.v2.OrigineType
import be.cin.mycarenet.esb.common.v2.PackageType
import be.cin.mycarenet.esb.common.v2.ValueRefString
import be.cin.nip.async.generic.GetResponse
import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.IOUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.taktik.connector.business.genericasync.builders.BuilderFactory
import org.taktik.connector.business.genericasync.service.impl.GenAsyncServiceImpl
import org.taktik.connector.business.mycarenetcommons.builders.util.BlobUtil
import org.taktik.connector.business.mycarenetcommons.mapper.SendRequestMapper
import org.taktik.connector.business.mycarenetdomaincommons.builders.RequestBuilderFactory
import org.taktik.connector.technical.config.ConfigFactory
import org.taktik.connector.technical.exception.TechnicalConnectorException
import org.taktik.connector.technical.handler.domain.WsAddressingHeader
import org.taktik.connector.technical.service.sts.security.impl.KeyStoreCredential
import org.taktik.connector.technical.utils.ConnectorIOUtils
import org.taktik.freehealth.middleware.dao.User
import org.taktik.freehealth.middleware.dto.efact.EfactMessage
import org.taktik.freehealth.middleware.dto.efact.EfactSendResponse
import org.taktik.freehealth.middleware.dto.efact.InvoicesBatch
import org.taktik.freehealth.middleware.format.efact.BelgianInsuranceInvoicingFormatReader
import org.taktik.freehealth.middleware.format.efact.BelgianInsuranceInvoicingFormatWriter
import org.taktik.freehealth.middleware.service.EfactService
import org.taktik.freehealth.middleware.service.STSService
import java.io.IOException
import java.io.StringWriter
import java.net.URI
import java.net.URISyntaxException
import java.text.DecimalFormat
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedList
import java.util.UUID
import javax.xml.ws.soap.SOAPFaultException

@Service
class EfactServiceImpl(private val stsService: STSService) : EfactService {
    private val config = ConfigFactory.getConfigValidator(listOf())
    private val genAsyncService = GenAsyncServiceImpl("invoicing")

    override fun makeFlatFile(batch: InvoicesBatch, isTest: Boolean): String {
        require(batch.numericalRef?.let { it <= 9999999999L } ?: false) { batch.numericalRef?.let { "numericalRef is too long (10 positions max)" } ?: "numericalRef is missing" }
        requireNotNull(batch.sender) { "Sender cannot be null" }
        requireNotNull(batch.batchRef) { "BatchRef cannot be null" }
        requireNotNull(batch.uniqueSendNumber) { "UniqueSendNumber cannot be null" }

        batch.invoices.forEach {
            requireNotNull(it.invoiceNumber) { "One of the invoices has an empty invoice number" }
            requireNotNull(it.invoiceRef) { "One of the invoices has an empty invoice ref" }
            requireNotNull(it.ioCode) { "One of the invoices has an empty io code" }
            requireNotNull(it.patient) { "One of the invoices has an empty patient" }
        }

        val stringWriter = StringWriter()
        val iv = BelgianInsuranceInvoicingFormatWriter(stringWriter)

        try {
            iv.write200and300(batch.sender!!, batch.numericalRef
                ?: 0, batch.batchRef!!, if (isTest) 92 else 12, batch.uniqueSendNumber!!, batch.invoicingYear, batch.invoicingMonth, isTest)

            val codes = ArrayList<Long>()
            var amount = 0L
            var recordsCount = 0L

            val codesPerOAMap = HashMap<String, MutableList<Long>>()
            val amountPerOAMap = HashMap<String, Array<Long>>()
            val recordsCountPerOAMap = HashMap<String, Array<Long>>()

            var rn =
                iv.writeFileHeader(1, batch.sender!!, if (isTest) 9991999L else 1999L, batch.uniqueSendNumber!!, batch.invoicingYear, batch.invoicingMonth, batch.batchRef!!)
            recordsCount++

            for (invoice in batch.invoices.sortedWith(Comparator { i1, i2 ->
                iv.getDestCode(i1.ioCode!!, batch.sender!!).compareTo(iv.getDestCode(i2.ioCode!!, batch.sender!!))
            })) {
                if (invoice.items.isNotEmpty()) {
                    val destCode = iv.getDestCode(invoice.ioCode!!, batch.sender!!)

                    val codesPerOA: MutableList<Long> = codesPerOAMap.getOrPut(destCode) { LinkedList() }
                    val amountPerOA: Array<Long> = amountPerOAMap.getOrPut(destCode) { arrayOf(0L) }  //An array to pass it by reference
                    val recordsCountPerOA: Array<Long> = recordsCountPerOAMap.getOrPut(destCode) { arrayOf(0L) }  //An array to pass it by reference

                    val recordCodes = ArrayList<Long>()
                    var recordAmount = 0L
                    var recordFee = 0L
                    var recordSup = 0L
                    rn =
                        iv.writeRecordHeader(rn, batch.sender!!, invoice.invoiceNumber!!, invoice.reason!!, invoice.invoiceRef!!, invoice.patient!!, invoice.ioCode!!, false, invoice.hospitalisedPatient)
                    recordsCountPerOA[0]++
                    recordsCount++
                    for (it in invoice.items) {
                        rn =
                            iv.writeRecordContent(rn, batch.sender!!, batch.invoicingYear, batch.invoicingMonth, invoice.invoiceRef!!, invoice.patient!!, invoice.ioCode!!, it)

                        recordsCountPerOA[0]++
                        recordsCount++

                        if (it.insuranceRef != null) {
                            rn =
                                iv.writeInvolvementRecordContent(rn, batch.invoicingYear, batch.invoicingMonth, invoice.patient!!, it)
                            recordCodes.add(it.codeNomenclature)
                            recordsCountPerOA[0]++
                            recordsCount++
                        }

                        if (it.eidItem != null) {
                            rn = iv.writeEid(rn, it, invoice.patient!!, batch.sender!!)
                            recordCodes.add(it.codeNomenclature)
                            recordsCountPerOA[0]++
                            recordsCount++
                        }

                        codesPerOA!!.add(it.codeNomenclature)
                        amountPerOA!![0] += it.reimbursedAmount

                        recordCodes.add(it.codeNomenclature)
                        recordAmount += it.reimbursedAmount
                        recordFee += it.patientFee
                        recordSup += it.doctorSupplement

                    }
                    rn =
                        iv.writeRecordFooter(rn, batch.sender!!, invoice.invoiceNumber!!, invoice.invoiceRef!!, invoice.patient!!, invoice.ioCode!!, recordCodes, recordAmount, recordFee, recordSup)
                    recordsCountPerOA[0]++
                    recordsCount++

                    codes.addAll(recordCodes)
                    amount += recordAmount
                }
            }
            iv.writeFileFooter(rn, batch.sender!!, batch.uniqueSendNumber, batch.invoicingYear, batch.invoicingMonth, codes, amount)
            recordsCount++

            for (k in codesPerOAMap.keys) {
                iv.write400(k, batch.numericalRef, recordsCountPerOAMap[k]!![0], codesPerOAMap[k]!!, amountPerOAMap[k]!![0])
            }
            iv.write960000(batch.ioFederationCode!!.replace(Regex("00$"), "99"), recordsCount, codes, amount)
        } catch (e: IOException) {
            throw IllegalArgumentException(e)
        }


        return stringWriter.toString()
    }

    override fun sendBatch(keystoreId: UUID, tokenId: UUID, passPhrase: String, batch: InvoicesBatch): EfactSendResponse {
        requireNotNull(keystoreId) { "Keystore id cannot be null" }
        requireNotNull(tokenId) { "Token id cannot be null" }
        val samlToken = stsService.getSAMLToken(tokenId, keystoreId, passPhrase) ?: throw IllegalArgumentException("Cannot obtain token for Ehealth Box operations")
        val keystore = stsService.getKeyStore(keystoreId, passPhrase)!!
        val credential = KeyStoreCredential(keystore, "authentication", passPhrase)

        val isTest = config.getProperty("endpoint.mcn.tarification").contains("-acpt")

        val fed = batch.ioFederationCode
        val inputReference = "" + DecimalFormat("00000000000000").format(batch.numericalRef ?: 0)
        val content = makeFlatFile(batch, isTest)

        val requestObjectBuilder = try {
            BuilderFactory.getRequestObjectBuilder("invoicing")
        } catch (e: Exception) {
            throw IllegalArgumentException(e)
        }

        val blob = RequestBuilderFactory.getBlobBuilder("invoicing").build(content.toByteArray(Charsets.UTF_8))

        val messageName = "HCPFAC" // depends on content of message HCPFAC HCPAFD or HCPVWR
        blob.messageName = messageName

        // Creation of the request

        val ci = CommonInput().apply {
            request = be.cin.mycarenet.esb.common.v2.RequestType().apply {
                isIsTest = isTest!!
            }
            origin =
                buildOriginType(batch.sender!!.nihii!!.toString(), batch.sender!!.ssin!!.toString(), batch.sender!!.firstName!!, batch.sender!!.lastName!!)
            this.inputReference = inputReference
        }

        val xades = BlobUtil.generateXades(SendRequestMapper.mapBlobToBlobType(blob), credential, "invoicing").value
        val post = requestObjectBuilder.buildPostRequest(ci, SendRequestMapper.mapBlobToCinBlob(blob), xades)
        val header: WsAddressingHeader
        try {
            header = WsAddressingHeader(URI("urn:be:cin:nip:async:generic:post:msg"))
            header.to = URI(if (fed != null) "urn:nip:destination:io:$fed" else "")
            header.faultTo = "http://www.w3.org/2005/08/addressing/anonymous"
            header.replyTo = "http://www.w3.org/2005/08/addressing/anonymous"
            header.messageID = URI("" + UUID.randomUUID())
        } catch (e: URISyntaxException) {
            throw IllegalStateException(e)
        }

        val postResponse = genAsyncService.postRequest(samlToken, post, header)
        val tack = postResponse.getReturn()
        val success = tack.resultMajor != null && tack.resultMajor == "urn:nip:tack:result:major:success"

        return EfactSendResponse(success, inputReference, tack)
    }

    override fun loadMessages(
        keystoreId: UUID,
        tokenId: UUID,
        passPhrase: String,
        hcpNihii: String,
        hcpSsin: String,
        hcpFirstName: String,
        hcpLastName: String,
        language: String
                    ): List<EfactMessage> {
        val samlToken =
            stsService.getSAMLToken(tokenId, keystoreId, passPhrase)
                ?: throw IllegalArgumentException("Cannot obtain token for Ehealth Box operations")

        val isTest = config.getProperty("endpoint.mcn.tarification").contains("-acpt")

        requireNotNull(keystoreId) { "Keystore id cannot be null" }
        requireNotNull(tokenId) { "Token id cannot be null" }

        val inputReference = "" + System.currentTimeMillis()
        val requestObjectBuilder = try {
            BuilderFactory.getRequestObjectBuilder("invoicing")
        } catch (e: Exception) {
            throw IllegalArgumentException(e)
        }

        val ci = CommonInput().apply {
            request = be.cin.mycarenet.esb.common.v2.RequestType().apply {
                isIsTest = isTest
            }
            origin = buildOriginType(hcpNihii, hcpSsin, hcpFirstName, hcpLastName)
            this.inputReference = inputReference
        }

        val header = try {
            WsAddressingHeader(URI("urn:be:cin:nip:async:generic:get:query")).apply {
                faultTo = "http://www.w3.org/2005/08/addressing/anonymous"
                replyTo = "http://www.w3.org/2005/08/addressing/anonymous"
                messageID = URI("" + UUID.randomUUID())
            }
        } catch (e: URISyntaxException) {
            throw IllegalStateException(e)
        }

        var batchSize = 16

        val eFactMessages = ArrayList<EfactMessage>()

        while (true) {
            val msgQuery = requestObjectBuilder.createMsgQuery(batchSize, true, "HCPFAC", "HCPAFD", "HCPVWR")
            val query = requestObjectBuilder.createQuery(batchSize, true)

            val getResponse: GetResponse
            try {
                getResponse =
                    genAsyncService.getRequest(samlToken, requestObjectBuilder.buildGetRequest(ci.origin, msgQuery, query), header)
            } catch (e: TechnicalConnectorException) {
                if ((e.message?.contains("SocketTimeout") == true) && batchSize > 1) {
                    batchSize /= 2
                    continue
                }
                throw IllegalStateException(e)
            } catch (e: SOAPFaultException) {
                if (e.message?.contains("Not enough time") == true) {
                    break
                }
                throw IllegalStateException(e)
            }

            eFactMessages += getResponse.getReturn().msgResponses.map { r ->
                EfactMessage().apply {
                    id = r.detail.id
                    name = r.detail.messageName
                    try {
                        detail =
                            String(ConnectorIOUtils.decompress(IOUtils.toByteArray(r.detail.value.inputStream)), Charsets.UTF_8) //This starts with 92...

                        message = BelgianInsuranceInvoicingFormatReader(language).read(this.detail!!)
                        xades = Base64.encodeBase64String(r.xadesT.value)
                    } catch (e: IOException) {}
                }
            } + getResponse.getReturn().tAckResponses.map { r ->
                EfactMessage().apply {
                    id = r.tAck.appliesTo.replace("urn:nip:reference:input:".toRegex(), "")
                    name = "tAck"
                    try {
                        tAck = r.tAck
                        xades = Base64.encodeBase64String(r.xadesT.value)
                    } catch (e: IOException) {}
                }
            }

            if (getResponse.getReturn().msgCount < batchSize && getResponse.getReturn().tAckCount < batchSize) {
                break
            } else {
                try {
                    Thread.sleep(7000)
                } catch (ignored: InterruptedException) {
                }

            }
        }
        return eFactMessages
    }

    private fun buildOriginType(nihii: String, ssin: String, firstName: String, lastName: String): OrigineType =
        OrigineType().apply {
            val principal = SecurityContextHolder.getContext().authentication?.principal as? User

            `package` = PackageType().apply {
                name = ValueRefString().apply { value = config.getProperty("genericasync.invoicing.package.name") }
                license = LicenseType().apply {
                    this.username = principal?.mcnLicense ?: config.getProperty("mycarenet.license.username")
                    this.password = principal?.mcnPassword ?: config.getProperty("mycarenet.license.password")
                }
            }
            careProvider = CareProviderType().apply {
                this.nihii = NihiiType().apply {
                    quality = "doctor"
                    value = ValueRefString().apply { value = nihii }
                }
                physicalPerson = IdType().apply {
                    this.ssin = ValueRefString().apply { value = ssin }
                    this.name = ValueRefString().apply { value = "$firstName $lastName" }
                }
            }
        }
}
