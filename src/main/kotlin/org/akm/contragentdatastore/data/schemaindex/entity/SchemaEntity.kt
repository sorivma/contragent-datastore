package org.akm.contragentdatastore.data.schemaindex.entity

import jakarta.persistence.*
import org.akm.contragentdatastore.data.schemaindex.converter.TableAttributeConverter
import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_schema_definitions")
data class SchemaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val schemaName: String,

    @Column(nullable = false, name = "table_definition", columnDefinition = "TEXT")
    @Convert(converter = TableAttributeConverter::class)
    val table: TableDefinition,

    val createdAt: LocalDateTime
)
