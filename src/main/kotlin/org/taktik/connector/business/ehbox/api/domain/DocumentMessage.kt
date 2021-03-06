/*
 *
 * Copyright (C) 2018 Taktik SA
 *
 * This file is part of FreeHealthConnector.
 *
 * FreeHealthConnector is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation.
 *
 * FreeHealthConnector is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FreeHealthConnector.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.taktik.connector.business.ehbox.api.domain

import java.util.ArrayList
import java.util.HashMap

open class DocumentMessage<T> : Message<T>() {
    var document: Document? = null
    var freeText: String? = null
        set(freeText) {
            if (freeText != null) {
                this.isHasFreeInformations = true
            }

            field = freeText
        }
    var freeInformationTableTitle: String? = null
    var freeInformationTableRows: MutableMap<String, String> = HashMap()
    var patientInss: String? = null
        set(patientInss) {
            if (patientInss != null) {
                this.isEncrypted = true
            }

            field = patientInss
        }
    private var annex: MutableList<Document>? = ArrayList()
    val copyMailTo: MutableList<String> = ArrayList()

    val documentTitle: String?
        get() = if (this.document == null) null else this.document!!.title

    val annexList: MutableList<Document>
        get() {
            if (this.annex == null) {
                this.annex = ArrayList()
            }

            return this.annex!!
        }
}
