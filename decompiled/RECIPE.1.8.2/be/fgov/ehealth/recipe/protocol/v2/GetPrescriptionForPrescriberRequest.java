package be.fgov.ehealth.recipe.protocol.v2;

import be.fgov.ehealth.commons.protocol.v1.RequestType;
import be.fgov.ehealth.recipe.core.v2.PrescriberServiceAdministrativeInformationType;
import be.fgov.ehealth.recipe.core.v2.SecuredContentType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "GetPrescriptionForPrescriberRequestType",
   propOrder = {"securedGetPrescriptionForPrescriberRequest", "administrativeInformation"}
)
@XmlRootElement(
   name = "GetPrescriptionForPrescriberRequest"
)
public class GetPrescriptionForPrescriberRequest extends RequestType {
   @XmlElement(
      name = "SecuredGetPrescriptionForPrescriberRequest",
      required = true
   )
   protected SecuredContentType securedGetPrescriptionForPrescriberRequest;
   @XmlElement(
      name = "AdministrativeInformation",
      required = true
   )
   protected PrescriberServiceAdministrativeInformationType administrativeInformation;

   public SecuredContentType getSecuredGetPrescriptionForPrescriberRequest() {
      return this.securedGetPrescriptionForPrescriberRequest;
   }

   public void setSecuredGetPrescriptionForPrescriberRequest(SecuredContentType value) {
      this.securedGetPrescriptionForPrescriberRequest = value;
   }

   public PrescriberServiceAdministrativeInformationType getAdministrativeInformation() {
      return this.administrativeInformation;
   }

   public void setAdministrativeInformation(PrescriberServiceAdministrativeInformationType value) {
      this.administrativeInformation = value;
   }
}
