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

package org.taktik.freehealth.middleware.web.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.taktik.freehealth.middleware.dto.consultrn.PersonMid
import org.taktik.freehealth.middleware.exception.MissingTokenException
import org.taktik.freehealth.middleware.service.ConsultRnService
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/consultrn")
class ConsultrnController(val consultRnService: ConsultRnService) {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MissingTokenException::class)
    @ResponseBody
    fun handleBadRequest(req: HttpServletRequest, ex: Exception): String = ex.message ?: "unknown reason"

    @GetMapping("/{ssin}", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun identify(
        @RequestHeader(name = "X-FHC-keystoreId") keystoreId: UUID,
        @RequestHeader(name = "X-FHC-tokenId") tokenId: UUID,
        @RequestHeader(name = "X-FHC-passPhrase") passPhrase: String,
        @PathVariable(value = "ssin") ssin: String
                ) = consultRnService.identify(keystoreId, tokenId, passPhrase, ssin)


    @GetMapping("/{dateOfBirth}/{lastName}", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun search(
        @RequestHeader(name = "X-FHC-keystoreId") keystoreId: UUID,
        @RequestHeader(name = "X-FHC-tokenId") tokenId: UUID,
        @RequestHeader(name = "X-FHC-passPhrase") passPhrase: String,
        @PathVariable(value = "dateOfBirth") dateOfBirth: Int,
        @PathVariable(value = "lastName") lastName: String,
        @RequestParam(required = false) firstName: String?,
        @RequestParam(required = false) middleName: String?,
        @RequestParam(required = false) gender: String?,
        @RequestParam(required = false) tolerance: Int?,
        @RequestParam(required = false) limit: Int?
              ) = consultRnService.search(keystoreId, tokenId, passPhrase, dateOfBirth, lastName, firstName, middleName, gender ?: "UNKNOWN", tolerance ?: 0, limit ?: 20)


    @PostMapping("", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun registerPerson(
        @RequestHeader(name = "X-FHC-keystoreId") keystoreId: UUID,
        @RequestHeader(name = "X-FHC-tokenId") tokenId: UUID,
        @RequestHeader(name = "X-FHC-passPhrase") passPhrase: String,
        @RequestBody mid: PersonMid
                      ) = consultRnService.registerPerson(keystoreId, tokenId, passPhrase, mid)


}
