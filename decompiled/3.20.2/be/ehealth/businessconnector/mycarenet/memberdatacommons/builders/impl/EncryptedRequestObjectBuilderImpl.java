package be.ehealth.businessconnector.mycarenet.memberdatacommons.builders.impl;

import be.cin.encrypted.BusinessContent;
import be.cin.encrypted.EncryptedKnownContent;
import be.ehealth.business.mycarenetcommons.mapper.v3.BlobMapper;
import be.ehealth.business.mycarenetcommons.mapper.v3.CommonInputMapper;
import be.ehealth.business.mycarenetdomaincommons.builders.BlobBuilder;
import be.ehealth.business.mycarenetdomaincommons.builders.BlobBuilderFactory;
import be.ehealth.business.mycarenetdomaincommons.builders.CommonBuilder;
import be.ehealth.business.mycarenetdomaincommons.builders.RequestBuilderFactory;
import be.ehealth.business.mycarenetdomaincommons.domain.Blob;
import be.ehealth.business.mycarenetdomaincommons.domain.InputReference;
import be.ehealth.business.mycarenetdomaincommons.util.McnConfigUtil;
import be.ehealth.businessconnector.mycarenet.memberdatacommons.builders.RequestObjectBuilder;
import be.ehealth.businessconnector.mycarenet.memberdatacommons.exception.MemberDataBusinessConnectorException;
import be.ehealth.businessconnector.mycarenet.memberdatacommons.exception.MemberDataBusinessConnectorExceptionValues;
import be.ehealth.businessconnector.mycarenet.memberdatacommons.security.MemberDataEncryptionUtil;
import be.ehealth.businessconnector.mycarenet.memberdatacommons.validator.MemberDataXmlValidatorImpl;
import be.ehealth.technicalconnector.config.ConfigFactory;
import be.ehealth.technicalconnector.config.impl.ConfigurationModuleBootstrap;
import be.ehealth.technicalconnector.exception.TechnicalConnectorException;
import be.ehealth.technicalconnector.exception.TechnicalConnectorExceptionValues;
import be.ehealth.technicalconnector.idgenerator.IdGeneratorFactory;
import be.ehealth.technicalconnector.service.keydepot.KeyDepotManager;
import be.ehealth.technicalconnector.service.keydepot.KeyDepotManagerFactory;
import be.ehealth.technicalconnector.utils.ConnectorXmlUtils;
import be.ehealth.technicalconnector.utils.SessionUtil;
import be.ehealth.technicalconnector.utils.impl.JaxbContextFactory;
import be.fgov.ehealth.messageservices.core.v1.SendTransactionRequest;
import be.fgov.ehealth.mycarenet.commons.core.v3.CommonInputType;
import be.fgov.ehealth.mycarenet.commons.core.v3.RoutingType;
import be.fgov.ehealth.mycarenet.memberdata.protocol.v1.MemberDataConsultationRequest;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptedRequestObjectBuilderImpl implements RequestObjectBuilder, ConfigurationModuleBootstrap.ModuleBootstrapHook {
   private static final Logger LOG = LoggerFactory.getLogger(EncryptedRequestObjectBuilderImpl.class);

   public MemberDataConsultationRequest buildConsultationRequest(boolean isTest, InputReference references, Object attrQuery) throws TechnicalConnectorException, MemberDataBusinessConnectorException {
      this.checkParameterNotNull(references, "InputReference");
      this.checkParameterNotNull(references.getInputReference(), "Input reference");
      String detailId = "_" + IdGeneratorFactory.getIdGenerator("uuid").generateId();
      byte[] attrQueryBytes = ConnectorXmlUtils.toByteArray(attrQuery);
      BlobBuilder blobBuilder = BlobBuilderFactory.getBlobBuilder("memberdata");
      BusinessContent businessContent = new BusinessContent();
      businessContent.setId(detailId);
      businessContent.setValue(attrQueryBytes);
      EncryptedKnownContent encryptedKnownContent = new EncryptedKnownContent();
      encryptedKnownContent.setReplyToEtk(KeyDepotManagerFactory.getKeyDepotManager().getETK(KeyDepotManager.EncryptionTokenType.HOLDER_OF_KEY).getEncoded());
      encryptedKnownContent.setBusinessContent(businessContent);

      try {
         attrQueryBytes = (new MemberDataEncryptionUtil()).handleEncryption(encryptedKnownContent, SessionUtil.getHolderOfKeyCrypto());
         if (attrQueryBytes != null && ConfigFactory.getConfigValidator().getBooleanProperty("be.ehealth.businessconnector.mycarenet.memberdatasync.builders.impl.dumpMessages", false)) {
            LOG.debug("EncryptedRequestObjectBuilder : Created blob content: " + new String(attrQueryBytes));
         }
      } catch (Exception var12) {
         throw new TechnicalConnectorException(TechnicalConnectorExceptionValues.ERROR_CRYPTO, var12, new Object[0]);
      }

      Blob blob = blobBuilder.build(attrQueryBytes, "none", detailId, "text/xml", "MDA", "encryptedForKnownBED");
      MemberDataConsultationRequest request = new MemberDataConsultationRequest();
      CommonBuilder commonBuilder = RequestBuilderFactory.getCommonBuilder("memberdata");
      request.setCommonInput(CommonInputMapper.mapCommonInputType(commonBuilder.createCommonInput(McnConfigUtil.retrievePackageInfo("memberdata"), isTest, references.getInputReference())));
      request.setId(IdGeneratorFactory.getIdGenerator("xsid").generateId());
      request.setIssueInstant(new DateTime());
      request.setDetail(BlobMapper.mapBlobTypefromBlob(blob));
      (new MemberDataXmlValidatorImpl()).validate(request);
      return request;
   }

   private void checkParameterNotNull(Object references, String parameterName) throws MemberDataBusinessConnectorException {
      if (references == null) {
         throw new MemberDataBusinessConnectorException(MemberDataBusinessConnectorExceptionValues.PARAMETER_NULL, new Object[]{parameterName});
      }
   }

   public void bootstrap() {
      JaxbContextFactory.initJaxbContext(MemberDataConsultationRequest.class);
      JaxbContextFactory.initJaxbContext(SendTransactionRequest.class);
      JaxbContextFactory.initJaxbContext(CommonInputType.class);
      JaxbContextFactory.initJaxbContext(RoutingType.class);
   }
}
