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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-EBIRTH-CAESAREANINDICATIONvalues.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-EBIRTH-CAESAREANINDICATIONvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="previouscaesareansection"/>
 *     &lt;enumeration value="breechpresentation"/>
 *     &lt;enumeration value="transversepresentation"/>
 *     &lt;enumeration value="foetaldistress"/>
 *     &lt;enumeration value="dystocienotinlabour"/>
 *     &lt;enumeration value="dystocieinlabourinsufficientdilatation"/>
 *     &lt;enumeration value="dystocieinlabourinsufficientexpulsion"/>
 *     &lt;enumeration value="maternalindication"/>
 *     &lt;enumeration value="abruptioplacentae"/>
 *     &lt;enumeration value="requestedbypatient"/>
 *     &lt;enumeration value="multiplepregnancy"/>
 *     &lt;enumeration value="other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "CD-EBIRTH-CAESAREANINDICATIONvalues")
@XmlEnum
public enum CDEBIRTHCAESAREANINDICATIONvalues {

    @XmlEnumValue("previouscaesareansection")
    PREVIOUSCAESAREANSECTION("previouscaesareansection"),
    @XmlEnumValue("breechpresentation")
    BREECHPRESENTATION("breechpresentation"),
    @XmlEnumValue("transversepresentation")
    TRANSVERSEPRESENTATION("transversepresentation"),
    @XmlEnumValue("foetaldistress")
    FOETALDISTRESS("foetaldistress"),
    @XmlEnumValue("dystocienotinlabour")
    DYSTOCIENOTINLABOUR("dystocienotinlabour"),
    @XmlEnumValue("dystocieinlabourinsufficientdilatation")
    DYSTOCIEINLABOURINSUFFICIENTDILATATION("dystocieinlabourinsufficientdilatation"),
    @XmlEnumValue("dystocieinlabourinsufficientexpulsion")
    DYSTOCIEINLABOURINSUFFICIENTEXPULSION("dystocieinlabourinsufficientexpulsion"),
    @XmlEnumValue("maternalindication")
    MATERNALINDICATION("maternalindication"),
    @XmlEnumValue("abruptioplacentae")
    ABRUPTIOPLACENTAE("abruptioplacentae"),
    @XmlEnumValue("requestedbypatient")
    REQUESTEDBYPATIENT("requestedbypatient"),
    @XmlEnumValue("multiplepregnancy")
    MULTIPLEPREGNANCY("multiplepregnancy"),
    @XmlEnumValue("other")
    OTHER("other");
    private final String value;

    CDEBIRTHCAESAREANINDICATIONvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDEBIRTHCAESAREANINDICATIONvalues fromValue(String v) {
        for (CDEBIRTHCAESAREANINDICATIONvalues c: CDEBIRTHCAESAREANINDICATIONvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
