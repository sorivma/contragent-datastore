package org.akm.contragentdatastore.data.schemaindex.entity

import com.fasterxml.jackson.databind.JsonNode
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "phisical_persons")
data class PhysicalPersonEntity(
    @Id
    @Column(name = "inn")
    val inn: String? = null,

    @Column(name = "ogrnip")
    val ogrnip: String? = null,

    @Column(name = "issue_date")
    val issueDate: LocalDate? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "citizenship_info", columnDefinition = "jsonb")
    val citizenshipInfo: JsonNode? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "registration_info", columnDefinition = "jsonb")
    val registrationInfo: JsonNode? = null,

    @Column(name = "activity_type_code")
    val activityTypeCode: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "registration_authority", columnDefinition = "jsonb")
    val registrationAuthority: JsonNode? = null,

    @Column(name = "activity_type_name")
    val activityTypeName: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "termination_info", columnDefinition = "jsonb")
    val terminationInfo: JsonNode? = null,

    @Column(name = "ogrnip_date")
    val ogrnipDate: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "egip_records", columnDefinition = "jsonb")
    val egipRecords: JsonNode? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "individual_info", columnDefinition = "jsonb")
    val individualInfo: JsonNode? = null
)
