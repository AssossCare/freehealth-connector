/*
 * Copyright (c) 2020. Taktik SA, All rights reserved.
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.06.14 at 03:48:48 PM CEST
//


package org.taktik.connector.business.domain.kmehr.v20120701.be.fgov.ehealth.standards.kmehr.cd.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for CD-CERTAINTY complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CD-CERTAINTY">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.ehealth.fgov.be/standards/kmehr/cd/v1>CD-CERTAINTYvalues">
 *       &lt;attribute name="S" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="CD-CERTAINTY" />
 *       &lt;attribute name="SV" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DN" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="L" type="{http://www.w3.org/2001/XMLSchema}language" default="en" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CD-CERTAINTY", propOrder = {
    "value"
})
public class CDCERTAINTY
    implements Serializable
{

    private final static long serialVersionUID = 20120701L;
    @XmlValue
    protected CDCERTAINTYvalues value;
    @XmlAttribute(name = "S", required = true)
    protected String s = "CD-CERTAINTY";
    @XmlAttribute(name = "SV", required = true)
    protected String sv = "1.0";
    @XmlAttribute(name = "DN")
    protected String dn;
    @XmlAttribute(name = "L")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "language")
    protected String l;

    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link CDCERTAINTYvalues }
     *
     */
    public CDCERTAINTYvalues getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link CDCERTAINTYvalues }
     *
     */
    public void setValue(CDCERTAINTYvalues value) {
        this.value = value;
    }

    /**
     * Gets the value of the s property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getS() {
        if (s == null) {
            return "CD-CERTAINTY";
        } else {
            return s;
        }
    }

    /**
     * Sets the value of the s property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setS(String value) {
        this.s = value;
    }

    /**
     * Gets the value of the sv property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSV() {
        return sv;
    }

    /**
     * Sets the value of the sv property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSV(String value) {
        this.sv = value;
    }

    /**
     * Gets the value of the dn property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDN() {
        return dn;
    }

    /**
     * Sets the value of the dn property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDN(String value) {
        this.dn = value;
    }

    /**
     * Gets the value of the l property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getL() {
        if (l == null) {
            return "en";
        } else {
            return l;
        }
    }

    /**
     * Sets the value of the l property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setL(String value) {
        this.l = value;
    }

}
