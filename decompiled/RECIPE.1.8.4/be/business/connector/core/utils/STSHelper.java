package be.business.connector.core.utils;

import be.apb.gfddpp.domain.PharmacyIdType;
import be.business.connector.core.exceptions.IntegrationModuleException;
import java.io.StringWriter;
import java.util.Calendar;
import javax.xml.bind.DatatypeConverter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang.Validate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class STSHelper {
   private static final String SAML_ATTRIBUTE_NAMESPACE = "AttributeNamespace";
   private static final String SAML_ATTRIBUTE_NAME = "AttributeName";
   public static final String SAML_CONDITIONS = "Conditions";
   public static final String SAML_NOTONORAFTER = "NotOnOrAfter";
   public static final String SAML_SUCCESS = "samlp:Success";
   public static final String SAML_STATUSCODE = "StatusCode";
   public static final String SAML_STATUSMESSAGE = "StatusMessage";
   public static final String SAML_VALUE = "Value";
   public static final String SAML_ASSERTION = "Assertion";
   public static final String SAML_ATTRIBUTESTATEMENT = "AttributeStatement";
   private static final String SAML_ATTRIBUTE = "Attribute";
   private static final String ASSERTION_URI = "urn:oasis:names:tc:SAML:1.0:assertion";
   private static final String PHARMACIST_NIHII_URI = "urn:be:fgov:ehealth:1.0:pharmacy:nihii-number";
   private static final String PHARMACIST_NIHII_URI1 = "urn:be:fgov:ehealth:1.0:nihii:pharmacy:nihii-number";
   private static final String MIDWIFE_NIHII_URI = "urn:be:fgov:person:ssin:ehealth:1.0:nihii:midwife:nihii11";
   private static final String MIDWIFE_NIHII_URI1 = "urn:be:fgov:person:ssin:ehealth:1.0:midwife:nihii11";
   private static final String PHYSIOTHERAPIST_NIHII_URI = "urn:be:fgov:person:ssin:ehealth:1.0:nihii:physiotherapist:nihii11";
   private static final String PHYSIOTHERAPIST_NIHII_URI1 = "urn:be:fgov:person:ssin:ehealth:1.0:physiotherapist:nihii11";
   private static final String NURSE_NIHII_URI = "urn:be:fgov:person:ssin:ehealth:1.0:nihii:nurse:nihii11";
   private static final String NURSE_NIHII_URI1 = "urn:be:fgov:person:ssin:ehealth:1.0:nurse:nihii11";
   private static final String DENTIST_NIHII_URI = "urn:be:fgov:person:ssin:ehealth:1.0:nihii:dentist:nihii11";
   private static final String DENTIST_NIHII_URI1 = "urn:be:fgov:person:ssin:ehealth:1.0:dentist:nihii11";
   private static final String INDENTIFICATION_NAME_SPACE = "urn:be:fgov:identification-namespace";
   private static final String CERTIFIERD_NAME_SPACE = "urn:be:fgov:certified-namespace:ehealth";
   private static final String DOCTOR_NIHII_URI = "urn:be:fgov:person:ssin:ehealth:1.0:doctor:nihii11";
   private static final String DOCTOR_NIHII_URI1 = "urn:be:fgov:person:ssin:ehealth:1.0:nihii:doctor:nihii11";
   private static final String HOSPITAL_NIHII_URI = "urn:be:fgov:ehealth:1.0:certificateholder:hospital:nihii-number";
   private static final String HOSPITAL_NIHII_URI1 = "urn:be:fgov:ehealth:1.0:hospital:nihii-number";

   public static String getStatusCode(Element stsResponse) {
      return stsResponse.getElementsByTagNameNS("*", "StatusCode").item(0).getAttributes().getNamedItem("Value").getNodeValue();
   }

   public static String getStatusMessage(Element stsResponse) {
      try {
         return stsResponse.getElementsByTagNameNS("*", "StatusMessage").item(0).getTextContent();
      } catch (Throwable var1) {
         return null;
      }
   }

   public static Calendar getNotOnOrAfterConditions(Element stsResponse) {
      return DatatypeConverter.parseDate(stsResponse.getElementsByTagNameNS("*", "Conditions").item(0).getAttributes().getNamedItem("NotOnOrAfter").getTextContent());
   }

   public static NodeList getConditions(Element stsResponse) {
      return stsResponse.getElementsByTagNameNS("*", "Conditions");
   }

   public static NodeList getAttributes(Element stsResponse) {
      return ((Element)stsResponse.getElementsByTagNameNS("*", "AttributeStatement").item(0)).getElementsByTagNameNS("*", "Attribute");
   }

   public static Element getAssertion(Element stsResponse) {
      return (Element)stsResponse.getElementsByTagNameNS("*", "Assertion").item(0);
   }

   public static String getNihii(Element element) throws IntegrationModuleException {
      Validate.notNull(element, "element should not be null");
      NodeList attributes = element.getElementsByTagNameNS("*", "Attribute");
      if (attributes.getLength() == 0) {
         attributes = element.getElementsByTagNameNS("urn:oasis:names:tc:SAML:1.0:assertion", "Attribute");
         if (attributes.getLength() == 0) {
            return null;
         }
      }

      for(int i = 0; i < attributes.getLength(); ++i) {
         Node node = attributes.item(i);
         String attributeName = node.getAttributes().getNamedItem("AttributeName").getTextContent();
         String attributeNamespace = node.getAttributes().getNamedItem("AttributeNamespace").getTextContent();
         if (("urn:be:fgov:person:ssin:ehealth:1.0:doctor:nihii11".equals(attributeName) || "urn:be:fgov:person:ssin:ehealth:1.0:nihii:doctor:nihii11".equals(attributeName)) && verifyNameSpace(attributeNamespace)) {
            return node.getTextContent().trim();
         }

         if (("urn:be:fgov:ehealth:1.0:certificateholder:hospital:nihii-number".equals(attributeName) || "urn:be:fgov:ehealth:1.0:hospital:nihii-number".equals(attributeName)) && verifyNameSpace(attributeNamespace)) {
            return node.getTextContent().trim();
         }

         if (("urn:be:fgov:person:ssin:ehealth:1.0:nihii:dentist:nihii11".equals(attributeName) || "urn:be:fgov:person:ssin:ehealth:1.0:dentist:nihii11".equals(attributeName)) && verifyNameSpace(attributeNamespace)) {
            return node.getTextContent().trim();
         }

         if (("urn:be:fgov:person:ssin:ehealth:1.0:nihii:midwife:nihii11".equals(attributeName) || "urn:be:fgov:person:ssin:ehealth:1.0:midwife:nihii11".equals(attributeName)) && verifyNameSpace(attributeNamespace)) {
            return node.getTextContent().trim();
         }

         if (("urn:be:fgov:person:ssin:ehealth:1.0:nihii:physiotherapist:nihii11".equals(attributeName) || "urn:be:fgov:person:ssin:ehealth:1.0:physiotherapist:nihii11".equals(attributeName)) && verifyNameSpace(attributeNamespace)) {
            return node.getTextContent().trim();
         }

         if (("urn:be:fgov:person:ssin:ehealth:1.0:nihii:nurse:nihii11".equals(attributeName) || "urn:be:fgov:person:ssin:ehealth:1.0:nurse:nihii11".equals(attributeName)) && verifyNameSpace(attributeNamespace)) {
            return node.getTextContent().trim();
         }

         if (("urn:be:fgov:ehealth:1.0:pharmacy:nihii-number".equals(attributeName) || "urn:be:fgov:ehealth:1.0:nihii:pharmacy:nihii-number".equals(attributeName)) && verifyNameSpace(attributeNamespace)) {
            return node.getTextContent().trim();
         }
      }

      throw new IntegrationModuleException(I18nHelper.getLabel("error.validation.saml.nihii.not.found", new String[]{prettify(element)}));
   }

   private static String prettify(Element doc) {
      try {
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty("omit-xml-declaration", "no");
         transformer.setOutputProperty("method", "xml");
         transformer.setOutputProperty("indent", "yes");
         transformer.setOutputProperty("encoding", "UTF-8");
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         StreamResult result = new StreamResult(new StringWriter());
         transformer.transform(new DOMSource(doc), result);
         return result.getWriter().toString();
      } catch (Exception var4) {
         return "";
      }
   }

   private static boolean verifyNameSpace(String attributeNamespace) {
      return "urn:be:fgov:certified-namespace:ehealth".equals(attributeNamespace) || "urn:be:fgov:identification-namespace".equals(attributeNamespace);
   }

   public static String getType(Element element) throws IntegrationModuleException {
      NodeList attributes = element.getElementsByTagNameNS("*", "Attribute");
      if (attributes.getLength() == 0) {
         attributes = element.getElementsByTagNameNS("urn:oasis:names:tc:SAML:1.0:assertion", "Attribute");
         if (attributes.getLength() == 0) {
            return null;
         }
      }

      for(int i = 0; i < attributes.getLength(); ++i) {
         Node node = attributes.item(i);
         String attributeName = node.getAttributes().getNamedItem("AttributeName").getTextContent();
         String attributeNamespace = node.getAttributes().getNamedItem("AttributeNamespace").getTextContent();
         if (("urn:be:fgov:person:ssin:ehealth:1.0:doctor:nihii11".equals(attributeName) || "urn:be:fgov:person:ssin:ehealth:1.0:nihii:doctor:nihii11".equals(attributeName)) && verifyNameSpace(attributeNamespace)) {
            return Qualities.DOCTOR.toString();
         }

         if ("urn:be:fgov:ehealth:1.0:pharmacy:nihii-number".equals(attributeName) && verifyNameSpace(attributeNamespace)) {
            return PharmacyIdType.NIHII.toString();
         }

         if (("urn:be:fgov:ehealth:1.0:certificateholder:hospital:nihii-number".equals(attributeName) || "urn:be:fgov:ehealth:1.0:hospital:nihii-number".equals(attributeName)) && verifyNameSpace(attributeNamespace)) {
            return Qualities.HOSPITAL.value();
         }
      }

      throw new IntegrationModuleException(I18nHelper.getLabel("error.validation.saml.type.not.found"));
   }

   public static String getNiss(Element element) {
      NodeList attributes = element.getElementsByTagNameNS("*", "Attribute");
      if (attributes.getLength() == 0) {
         attributes = element.getElementsByTagNameNS("urn:oasis:names:tc:SAML:1.0:assertion", "Attribute");
         if (attributes.getLength() == 0) {
            return null;
         }
      }

      for(int i = 0; i < attributes.getLength(); ++i) {
         Node node = attributes.item(i);
         String attributeName = node.getAttributes().getNamedItem("AttributeName").getTextContent();
         String attributeNamespace = node.getAttributes().getNamedItem("AttributeNamespace").getTextContent();
         if ("urn:be:fgov:person:ssin".equals(attributeName) && "urn:be:fgov:identification-namespace".equals(attributeNamespace)) {
            return node.getTextContent().trim();
         }
      }

      return null;
   }

   public static Element convert(Source stsResponse) throws IntegrationModuleException {
      try {
         StringWriter stringWriter = new StringWriter();
         Result result = new StreamResult(stringWriter);
         TransformerFactory factory = TransformerFactory.newInstance();
         Transformer transformer = factory.newTransformer();
         transformer.transform(stsResponse, result);
         String xmlResponse = stringWriter.getBuffer().toString();
         return SAML10Converter.toElement(xmlResponse);
      } catch (TransformerException var6) {
         throw new IntegrationModuleException(var6);
      }
   }
}
