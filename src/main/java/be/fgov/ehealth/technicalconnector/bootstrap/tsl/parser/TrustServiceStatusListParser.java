package be.fgov.ehealth.technicalconnector.bootstrap.tsl.parser;

import org.taktik.connector.technical.enumeration.Charset;
import org.taktik.connector.technical.exception.TechnicalConnectorException;
import org.taktik.connector.technical.exception.TechnicalConnectorExceptionValues;
import org.taktik.connector.technical.utils.ConnectorIOUtils;
import org.taktik.connector.technical.utils.DateUtils;
import be.fgov.ehealth.technicalconnector.bootstrap.utils.BootStrapUtils;
import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.joda.time.DateTime;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TrustServiceStatusListParser {
   private static final String TSL_NS = "http://uri.etsi.org/02231/v2#";
   private static final QName QN_TSL_X509CERTIFICATE = new QName("http://uri.etsi.org/02231/v2#", "X509Certificate");
   private static final QName QN_TSL_TSPSERVICE = new QName("http://uri.etsi.org/02231/v2#", "TSPService");
   private static final QName QN_TSL_TRUSTSERVICEPROVIDER = new QName("http://uri.etsi.org/02231/v2#", "TrustServiceProvider");
   private static final QName QN_TSL_SERVICETYPEIDENTIFIER = new QName("http://uri.etsi.org/02231/v2#", "ServiceTypeIdentifier");
   private static final QName QN_TSL_NEXTUPDATE = new QName("http://uri.etsi.org/02231/v2#", "NextUpdate");
   private static final QName QN_TSL_DATETIME = new QName("http://uri.etsi.org/02231/v2#", "dateTime");
   private TrustServiceStatusListParser.SaxHandler handler;

   public void parse(String xml, String... serviceTypeIdentifiers) throws TechnicalConnectorException {
      ByteArrayInputStream is = new ByteArrayInputStream(ConnectorIOUtils.toBytes(xml, Charset.UTF_8));

      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
         factory.setNamespaceAware(true);
         SAXParser saxParser = factory.newSAXParser();
         this.handler = new TrustServiceStatusListParser.SaxHandler(serviceTypeIdentifiers);
         saxParser.parse(is, this.handler);
      } catch (Exception var6) {
         throw new TechnicalConnectorException(TechnicalConnectorExceptionValues.ERROR_TECHNICAL, var6, new Object[0]);
      }
   }

   public List<X509Certificate> getTrustedList() {
      return this.handler.getResult();
   }

   public DateTime nextUpdate() {
      return this.handler.nextUpdate();
   }

   static class SaxHandler extends DefaultHandler {
      private boolean trustServiceProvider;
      private boolean tspService;
      private boolean serviceType;
      private boolean harvestX509;
      private boolean update;
      private boolean datetime;
      private boolean x509;
      private List<String> serviceTypeIdentifiers;
      private List<X509Certificate> result = new ArrayList();
      private DateTime nextUpdate;

      public SaxHandler(String... serviceTypeIdentifiers) {
         this.serviceTypeIdentifiers = Arrays.asList(serviceTypeIdentifiers);
      }

      public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
         if ("http://uri.etsi.org/02231/v2#".equals(uri)) {
            if (localName.equals(TrustServiceStatusListParser.QN_TSL_NEXTUPDATE.getLocalPart())) {
               this.update = true;
            } else if (localName.equals(TrustServiceStatusListParser.QN_TSL_DATETIME.getLocalPart())) {
               this.datetime = true;
            } else if (localName.equals(TrustServiceStatusListParser.QN_TSL_TRUSTSERVICEPROVIDER.getLocalPart())) {
               this.trustServiceProvider = true;
            } else if (this.trustServiceProvider && localName.equals(TrustServiceStatusListParser.QN_TSL_TSPSERVICE.getLocalPart())) {
               this.tspService = true;
            } else if (this.tspService && localName.equals(TrustServiceStatusListParser.QN_TSL_SERVICETYPEIDENTIFIER.getLocalPart())) {
               this.serviceType = true;
            } else if (this.harvestX509 && localName.equals(TrustServiceStatusListParser.QN_TSL_X509CERTIFICATE.getLocalPart())) {
               this.x509 = true;
            }
         }

      }

      public void characters(char[] ch, int start, int length) throws SAXException {
         String value = (new String(ch, start, length)).trim();
         if (this.update && this.datetime) {
            this.nextUpdate = DateUtils.parseDateTime(value);
         } else if (this.serviceType && !this.harvestX509 && this.serviceTypeIdentifiers.contains(value)) {
            this.harvestX509 = true;
         } else if (this.x509) {
            try {
               this.result.add(BootStrapUtils.generateX509Cert(value));
            } catch (TechnicalConnectorException var6) {
               throw new SAXException(var6);
            }
         }

      }

      public void endElement(String uri, String localName, String qName) throws SAXException {
         if ("http://uri.etsi.org/02231/v2#".equals(uri)) {
            if (localName.equals(TrustServiceStatusListParser.QN_TSL_NEXTUPDATE.getLocalPart())) {
               this.update = false;
            } else if (this.update && localName.equals(TrustServiceStatusListParser.QN_TSL_DATETIME.getLocalPart())) {
               this.datetime = false;
            } else if (this.trustServiceProvider && localName.equals(TrustServiceStatusListParser.QN_TSL_TRUSTSERVICEPROVIDER.getLocalPart())) {
               this.trustServiceProvider = false;
            } else if (this.tspService && localName.equals(TrustServiceStatusListParser.QN_TSL_TSPSERVICE.getLocalPart())) {
               this.tspService = false;
               this.harvestX509 = false;
            } else if (this.serviceType && localName.equals(TrustServiceStatusListParser.QN_TSL_SERVICETYPEIDENTIFIER.getLocalPart())) {
               this.serviceType = false;
            } else if (this.x509 && localName.equals(TrustServiceStatusListParser.QN_TSL_X509CERTIFICATE.getLocalPart())) {
               this.x509 = false;
            }
         }

      }

      public List<X509Certificate> getResult() {
         return this.result;
      }

      public DateTime nextUpdate() {
         return this.nextUpdate;
      }
   }
}
