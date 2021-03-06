package be.recipe.services.prescriber;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "sendNotification",
   propOrder = {"sendNotificationParamSealed", "programIdentification", "mguid"}
)
@XmlRootElement(
   name = "sendNotification"
)
public class SendNotification {
   @XmlElement(
      name = "SendNotificationParamSealed",
      required = true
   )
   protected byte[] sendNotificationParamSealed;
   @XmlElement(
      name = "ProgramIdentification",
      required = true
   )
   protected String programIdentification;
   @XmlElement(
      name = "Mguid",
      required = true
   )
   protected String mguid;

   public byte[] getSendNotificationParamSealed() {
      return this.sendNotificationParamSealed;
   }

   public void setSendNotificationParamSealed(byte[] value) {
      this.sendNotificationParamSealed = value;
   }

   public String getProgramIdentification() {
      return this.programIdentification;
   }

   public void setProgramIdentification(String value) {
      this.programIdentification = value;
   }

   public String getMguid() {
      return this.mguid;
   }

   public void setMguid(String value) {
      this.mguid = value;
   }

}
