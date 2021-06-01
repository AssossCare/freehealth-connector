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
 * <p>Java class for CD-CARENET-PERSONAL-PARTvalues.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-CARENET-PERSONAL-PARTvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="code1"/>
 *     &lt;enumeration value="code2"/>
 *     &lt;enumeration value="future"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "CD-CARENET-PERSONAL-PARTvalues")
@XmlEnum
public enum CDCARENETPERSONALPARTvalues {

    @XmlEnumValue("code1")
    CODE_1("code1"),
    @XmlEnumValue("code2")
    CODE_2("code2"),
    @XmlEnumValue("future")
    FUTURE("future");
    private final String value;

    CDCARENETPERSONALPARTvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDCARENETPERSONALPARTvalues fromValue(String v) {
        for (CDCARENETPERSONALPARTvalues c: CDCARENETPERSONALPARTvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
