package be.fgov.ehealth.recipe.protocol.v4;

import be.fgov.ehealth.commons.protocol.v2.StatusResponseType;
import be.fgov.ehealth.recipe.core.v4.SecuredContentType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "PutVisionForPrescriberResponseType",
   propOrder = {"securedPutVisionForPrescriberResponse"}
)
@XmlRootElement(
   name = "PutVisionForPrescriberResponse"
)
public class PutVisionForPrescriberResponse extends StatusResponseType {
   @XmlElement(
      name = "SecuredPutVisionForPrescriberResponse"
   )
   protected SecuredContentType securedPutVisionForPrescriberResponse;

   public SecuredContentType getSecuredPutVisionForPrescriberResponse() {
      return this.securedPutVisionForPrescriberResponse;
   }

   public void setSecuredPutVisionForPrescriberResponse(SecuredContentType value) {
      this.securedPutVisionForPrescriberResponse = value;
   }
}
