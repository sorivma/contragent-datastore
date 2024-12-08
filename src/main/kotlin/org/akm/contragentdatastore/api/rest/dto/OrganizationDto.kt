package org.akm.contragentdatastore.api.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate

data class OrganizationDto(
    @JsonProperty("ИНН")
    val inn: String?,

    @JsonProperty("КПП")
    val kpp: String?,

    @JsonProperty("ОГРН")
    val ogrn: String?,

    @JsonProperty("КодОПФ")
    val opfCode: String?,

    @JsonProperty("СпрОПФ")
    val opfType: String?,

    @JsonProperty("ДатаВып")
    val issuanceDate: LocalDate?,

    @JsonProperty("СвОбрЮЛ")
    val creationInfo: JsonNode?,

    @JsonProperty("ДатаОГРН")
    val registrationDate: LocalDate?,

    @JsonProperty("СвНаимЮЛ")
    val nameInfo: JsonNode?,

    @JsonProperty("СвРегОрг")
    val registrationAuthority: JsonNode?,

    @JsonProperty("СвСтатус")
    val statusInfo: JsonNode?,

    @JsonProperty("СвАдресЮЛ")
    val addressInfo: JsonNode?,

    @JsonProperty("СвУчредит")
    val founderInfo: JsonNode?,

    @JsonProperty("СвЗапЕГРЮЛ")
    val egrulRecords: JsonNode?,

    @JsonProperty("ПолнНаимОПФ")
    val fullOpfName: JsonNode?,

    @JsonProperty("СведДолжнФЛ")
    val officerInfo: JsonNode?
)


