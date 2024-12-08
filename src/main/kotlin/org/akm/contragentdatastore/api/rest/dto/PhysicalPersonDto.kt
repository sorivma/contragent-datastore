package org.akm.contragentdatastore.api.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate

data class PhysicalPersonDto(
    @JsonProperty("СвФЛ")
    val individualInfo: JsonNode?,

    @JsonProperty("ИННФЛ")
    val inn: String?,

    @JsonProperty("ОГРНИП")
    val ogrnip: String?,

    @JsonProperty("ДатаВып")
    val issueDate: LocalDate?,

    @JsonProperty("СвГражд")
    val citizenshipInfo: JsonNode?,

    @JsonProperty("СвРегИП")
    val registrationInfo: JsonNode?,

    @JsonProperty("КодВидИП")
    val activityTypeCode: String?,

    @JsonProperty("СвРегОрг")
    val registrationAuthority: JsonNode?,

    @JsonProperty("НаимВидИП")
    val activityTypeName: String?,

    @JsonProperty("СвПрекращ")
    val terminationInfo: JsonNode?,

    @JsonProperty("ДатаОГРНИП")
    val ogrnipDate: String?, // Use LocalDate if date format is consistent

    @JsonProperty("СвЗапЕГРИП")
    val egipRecords: JsonNode?
)
