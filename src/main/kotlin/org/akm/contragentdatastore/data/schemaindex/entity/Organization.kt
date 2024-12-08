package org.akm.contragentdatastore.data.schemaindex.entity

import com.fasterxml.jackson.databind.JsonNode
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDate

@Entity
@Table(name = "organizations")
data class Organization(
    @Id
    @Column(name = "inn", nullable = false, length = 12)
    val inn: String,

    @Column(name = "kpp", nullable = false, length = 9)
    val kpp: String,

    @Column(name = "ogrn", nullable = false, length = 15)
    val ogrn: String,

    @Column(name = "kod_opf")
    val kodOpf: String? = null,

    @Column(name = "spr_opf")
    val sprOpf: String? = null,

    @Column(name = "data_vyp")
    val dataVyp: LocalDate? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sv_obr_yul", columnDefinition = "jsonb")
    val svObrYul: JsonNode? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sv_naim_yul", columnDefinition = "jsonb")
    val svNaimYul: JsonNode? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sv_reg_org", columnDefinition = "jsonb")
    val svRegOrg: JsonNode? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sv_adres_yul", columnDefinition = "jsonb")
    val svAdresYul: JsonNode? = null
)
