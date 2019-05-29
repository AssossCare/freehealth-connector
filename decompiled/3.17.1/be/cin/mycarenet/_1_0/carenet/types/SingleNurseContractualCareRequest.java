package be.cin.mycarenet._1_0.carenet.types;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "SingleNurseContractualCareRequestType",
   propOrder = {"careReceiverId", "nurseContractualCareRequestDetail"}
)
@XmlRootElement(
   name = "SingleNurseContractualCareRequest"
)
public class SingleNurseContractualCareRequest implements Serializable {
   private static final long serialVersionUID = 1L;
   @XmlElement(
      name = "CareReceiverId",
      required = true
   )
   protected ExtCareReceiverStrictIdType careReceiverId;
   @XmlElement(
      name = "NurseContractualCareRequestDetail",
      required = true
   )
   protected NurseContractualCareRequestDetail nurseContractualCareRequestDetail;
   @XmlAttribute(
      name = "MessageName"
   )
   protected MessageNameType messageName;
   @XmlAttribute(
      name = "Version"
   )
   protected String version;
   @XmlAttribute(
      name = "Duplicate"
   )
   protected Boolean duplicate;
   @XmlAttribute(
      name = "TestFlag"
   )
   protected Boolean testFlag;
   @XmlAttribute(
      name = "SenderReference"
   )
   protected String senderReference;
   @XmlAttribute(
      name = "ReceiverReference"
   )
   protected String receiverReference;
   @XmlAttribute(
      name = "Synchronous"
   )
   protected Boolean synchronous;

   public ExtCareReceiverStrictIdType getCareReceiverId() {
      return this.careReceiverId;
   }

   public void setCareReceiverId(ExtCareReceiverStrictIdType value) {
      this.careReceiverId = value;
   }

   public NurseContractualCareRequestDetail getNurseContractualCareRequestDetail() {
      return this.nurseContractualCareRequestDetail;
   }

   public void setNurseContractualCareRequestDetail(NurseContractualCareRequestDetail value) {
      this.nurseContractualCareRequestDetail = value;
   }

   public MessageNameType getMessageName() {
      return this.messageName;
   }

   public void setMessageName(MessageNameType value) {
      this.messageName = value;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String value) {
      this.version = value;
   }

   public Boolean isDuplicate() {
      return this.duplicate;
   }

   public void setDuplicate(Boolean value) {
      this.duplicate = value;
   }

   public Boolean isTestFlag() {
      return this.testFlag;
   }

   public void setTestFlag(Boolean value) {
      this.testFlag = value;
   }

   public String getSenderReference() {
      return this.senderReference;
   }

   public void setSenderReference(String value) {
      this.senderReference = value;
   }

   public String getReceiverReference() {
      return this.receiverReference;
   }

   public void setReceiverReference(String value) {
      this.receiverReference = value;
   }

   public Boolean isSynchronous() {
      return this.synchronous;
   }

   public void setSynchronous(Boolean value) {
      this.synchronous = value;
   }
}
