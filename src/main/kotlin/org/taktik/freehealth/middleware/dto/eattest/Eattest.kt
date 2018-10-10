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

package org.taktik.freehealth.middleware.dto.eattest

import java.io.Serializable

class Eattest(var codes: List<EattestCode> = listOf()) : Serializable {
    class EattestCode(
        var date: Int? = null,
        var riziv: String? = null,
        var quantity: Int = 1,
        var relativeService: String? = null,
        var reimbursement: Double? = null,
        var reglementarySupplement: Double? = null,
        var fee: Double? = null, // Sum of the 2 above
        var doctorSupplement: Double? = null,
        var norm: Int = 0,
        var cardReading: EattestCardReading? = null,
        var requestor: EattestRequestor? = null,
        var location: EattestHcParty? = null,
        var internship: EattestHcParty? = null,
        var gmdManager: EattestHcParty? = null
    ) : Serializable

    class EattestCardReading(
        var date: Int? = null,
        var time: Int? = null,
        var mediaType: Int = 1,
        var inputType: Int = 1,
        var manualInputReason: Int? = null,
        var serial: String? = null
    ) : Serializable

    class EattestHcParty(
        var idHcParty: String? = null,
        var idSsin: String? = null,
        var cdHcParty: String? = null,
        var firstName: String? = null,
        var lastName: String? = null
    ) : Serializable

    class EattestRequestor(var date: Int? = null, var hcp: EattestHcParty? = null) : Serializable
}
