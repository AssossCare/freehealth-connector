package be.ehealth.businessconnector.mycarenet.attestv2.builders.impl;

import be.cin.encrypted.EncryptedKnownContent;
import be.ehealth.business.mycarenetcommons.mapper.v3.BlobMapper;
import be.ehealth.business.mycarenetdomaincommons.domain.Blob;
import be.ehealth.businessconnector.mycarenet.attestv2.builders.ResponseObjectBuilder;
import be.ehealth.businessconnector.mycarenet.attestv2.domain.SignedBuilderResponse;
import be.ehealth.businessconnector.mycarenet.attestv2.domain.SignedEncryptedBuilderResponse;
import be.ehealth.technicalconnector.config.impl.ConfigurationModuleBootstrap;
import be.ehealth.technicalconnector.exception.TechnicalConnectorException;
import be.ehealth.technicalconnector.service.etee.Crypto;
import be.ehealth.technicalconnector.utils.ConnectorXmlUtils;
import be.ehealth.technicalconnector.utils.MarshallerHelper;
import be.ehealth.technicalconnector.utils.SessionUtil;
import be.ehealth.technicalconnector.utils.impl.JaxbContextFactory;
import be.fgov.ehealth.mycarenet.attest.protocol.v2.CancelAttestationResponse;
import be.fgov.ehealth.mycarenet.attest.protocol.v2.SendAttestationResponse;
import be.fgov.ehealth.mycarenet.commons.core.v3.BlobType;
import be.fgov.ehealth.mycarenet.commons.protocol.v3.SendResponseType;
import be.fgov.ehealth.standards.kmehr.schema.v1.Kmehrmessage;
import be.fgov.ehealth.technicalconnector.signature.AdvancedElectronicSignatureEnumeration;
import be.fgov.ehealth.technicalconnector.signature.SignatureBuilder;
import be.fgov.ehealth.technicalconnector.signature.SignatureBuilderFactory;
import be.fgov.ehealth.technicalconnector.signature.domain.SignatureVerificationResult;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ResponseObjectBuilderImpl implements ResponseObjectBuilder, ConfigurationModuleBootstrap.ModuleBootstrapHook {
   public final SignedEncryptedBuilderResponse handleSendResponseType(SendResponseType sendResponse) throws TechnicalConnectorException {
      BlobType blobType = sendResponse.getReturn().getDetail();
      Blob blob = BlobMapper.mapBlobfromBlobType(blobType);
      byte[] unsealedData = SessionUtil.getHolderOfKeyCrypto().unseal(Crypto.SigningPolicySelector.WITHOUT_NON_REPUDIATION, blob.getContent()).getContentAsByte();
      EncryptedKnownContent encryptedKnownContent = (EncryptedKnownContent)(new MarshallerHelper(EncryptedKnownContent.class, EncryptedKnownContent.class)).toObject(unsealedData);
      byte[] signature = encryptedKnownContent.getXades();
      byte[] signedData = ConnectorXmlUtils.toByteArray((Object)encryptedKnownContent);
      SignatureVerificationResult signatureVerificationResult = this.verifySignature(signature, signedData);
      return new SignedEncryptedBuilderResponse(encryptedKnownContent, signatureVerificationResult, unsealedData, ArrayUtils.clone(signature));
   }

   private SignatureVerificationResult verifySignature(byte[] signature, byte[] signedData) throws TechnicalConnectorException {
      Map<String, Object> options = Collections.emptyMap();
      SignatureBuilder builder = SignatureBuilderFactory.getSignatureBuilder(AdvancedElectronicSignatureEnumeration.XAdES);
      return builder.verify(signedData, signature, options);
   }

   public SignedBuilderResponse handleCancelResponseType(SendResponseType sendResponse) throws TechnicalConnectorException {
      byte[] signature = sendResponse.getReturn().getXadesT().getValue();
      Document explodedDoc = this.addSignatureToSendResponseType(sendResponse, signature);
      byte[] signedData = ConnectorXmlUtils.toByteArray((Node)explodedDoc);
      SignatureVerificationResult signatureVerificationResult = this.verifySignature(signature, signedData);
      return new SignedBuilderResponse(sendResponse.getReturn().getDetail().getValue(), signatureVerificationResult, signedData);
   }

   private Document addSignatureToSendResponseType(SendResponseType sendResponse, byte[] signature) throws TechnicalConnectorException {
      Element sigElement = ConnectorXmlUtils.toElement(ArrayUtils.clone(signature));
      Document explodedDoc = ConnectorXmlUtils.toDocument((Object)sendResponse);
      explodedDoc.adoptNode(sigElement);
      return explodedDoc;
   }

   public void bootstrap() {
      JaxbContextFactory.initJaxbContext(BlobType.class);
      JaxbContextFactory.initJaxbContext(SendAttestationResponse.class);
      JaxbContextFactory.initJaxbContext(CancelAttestationResponse.class);
      JaxbContextFactory.initJaxbContext(SendResponseType.class);
      JaxbContextFactory.initJaxbContext(Kmehrmessage.class);
   }
}
