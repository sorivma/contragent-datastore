package org.akm.contragentdatastore.api.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate

data class PhisicalPersonDto(
    @JsonProperty("СвФЛ")
    val individualInfo: IndividualInfoDto?,

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
    val egipRecords: List<JsonNode>?
)

data class IndividualInfoDto(
    @JsonProperty("Пол")
    val gender: String?,

    @JsonProperty("ФИОРус")
    val fullName: FullNameDto?,

    @JsonProperty("ГРНИПДата")
    val grnipDate: GrnipDateDto?
)

data class FullNameDto(
    @JsonProperty("Имя")
    val firstName: String?,

    @JsonProperty("Фамилия")
    val lastName: String?,

    @JsonProperty("Отчество")
    val middleName: String?
)

data class GrnipDateDto(
    @JsonProperty("ГРНИП")
    val grnip: String?,

    @JsonProperty("ДатаЗаписи")
    val recordDate: String? // Use LocalDate if date format is consistent
)