package be.fgov.ehealth.rn.commons.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "SsinType",
   propOrder = {"value"}
)
public class SsinType implements Serializable {
   private static final long serialVersionUID = 1L;
   @XmlValue
   protected String value;
   @XmlAttribute(
      name = "Encoded"
   )
   protected Boolean encoded;

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public Boolean isEncoded() {
      return this.encoded;
   }

   public void setEncoded(Boolean value) {
      this.encoded = value;
   }
}
