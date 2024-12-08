package org.akm.contragentdatastore.data.schemaindex

import jakarta.persistence.*
import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "schema_definitions")
data class SchemaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val schemaName: String,

    @Column(nullable = false, name = "table_definition")
    @Convert(converter = TableAttributeConverter::class)
    val table: TableDefinition,

    val createdAt: LocalDateTime
)
