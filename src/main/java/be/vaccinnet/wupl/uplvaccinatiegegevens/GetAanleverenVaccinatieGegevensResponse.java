//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.02.03 at 10:56:17 AM CET 
//


package be.vaccinnet.wupl.uplvaccinatiegegevens;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="certificaatStatus" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *           &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;/sequence&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="foutCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="foutOmschrijving" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;/sequence&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "certificaatStatus",
    "transactionId",
    "foutCode",
    "foutOmschrijving"
})
@XmlRootElement(name = "GetAanleverenVaccinatieGegevensResponse")
public class GetAanleverenVaccinatieGegevensResponse {

    protected Boolean certificaatStatus;
    protected String transactionId;
    protected String foutCode;
    protected String foutOmschrijving;

    /**
     * Gets the value of the certificaatStatus property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCertificaatStatus() {
        return certificaatStatus;
    }

    /**
     * Sets the value of the certificaatStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCertificaatStatus(Boolean value) {
        this.certificaatStatus = value;
    }

    /**
     * Gets the value of the transactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the foutCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFoutCode() {
        return foutCode;
    }

    /**
     * Sets the value of the foutCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFoutCode(String value) {
        this.foutCode = value;
    }

    /**
     * Gets the value of the foutOmschrijving property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFoutOmschrijving() {
        return foutOmschrijving;
    }

    /**
     * Sets the value of the foutOmschrijving property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFoutOmschrijving(String value) {
        this.foutOmschrijving = value;
    }

}
