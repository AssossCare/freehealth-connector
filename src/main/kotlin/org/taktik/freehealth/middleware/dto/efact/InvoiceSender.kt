/*
 * Copyright (C) 2018 Taktik SA
 *
 * This file is part of iCureBackend.
 *
 * iCureBackend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * iCureBackend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with iCureBackend.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.taktik.freehealth.middleware.dto.efact

class InvoiceSender {
    var nihii: Long? = null
    var bic: String? = null
    var iban: String? = null
    var bce: Long? = 999999922L

    var ssin: String? = null
    var lastName: String? = null
    var firstName: String? = null
    var phoneNumber: Long? = null
    var conventionCode: Int? = null

    val isSpecialist: Boolean
        get() = nihii!! % 1000L >= 10
}
