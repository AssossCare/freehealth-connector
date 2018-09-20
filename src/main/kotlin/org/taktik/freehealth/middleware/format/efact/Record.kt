package org.taktik.freehealth.middleware.format.efact

import org.taktik.freehealth.middleware.format.efact.segments.RecordOrSegmentDescription
import org.taktik.freehealth.middleware.format.efact.segments.Segment200Description

class Record<T:RecordOrSegmentDescription>(val description: T, val zones: List<Zone> = ArrayList()) {
    override fun toString(): String {
        return description.toString()+":\n"+zones.map { "\t"+it.toString() }.joinToString("\n")
    }
}
