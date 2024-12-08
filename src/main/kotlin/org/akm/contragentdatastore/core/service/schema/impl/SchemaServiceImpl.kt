package org.akm.contragentdatastore.core.service.schema.impl

import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.akm.contragentdatastore.core.service.schema.SchemaService
import org.akm.contragentdatastore.api.rest.dto.DefineSchemaRequest
import org.akm.contragentdatastore.core.service.util.ClickHouseQueryUtil.toClickHouseQuery
import org.akm.contragentdatastore.data.schemaindex.entity.SchemaEntity
import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition
import org.akm.contragentdatastore.data.schemaindex.repository.SchemaRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SchemaServiceImpl(
    private val schemaRepository: SchemaRepository,
    @Qualifier("clickhouse")
    private val jdbcTemplate: JdbcTemplate,
) : SchemaService {
    private val logger: Logger = LoggerFactory.getLogger(SchemaServiceImpl::class.java)

    @Transactional
    override fun defineSchema(schemaRequest: DefineSchemaRequest): String {
        val schemaEntity = SchemaEntity(
            schemaName = schemaRequest.schema,
            table = schemaRequest.tableDefinition,
            createdAt = LocalDateTime.now()
        )
        val saved = schemaRepository.saveAndFlush(schemaEntity)

        logger.info("Creating schema via ${saved.toClickHouseQuery()}")
        jdbcTemplate.execute(saved.toClickHouseQuery())

        return saved.schemaName
    }

    override fun getSchemas(): List<SchemaEntity> {
        return schemaRepository.findAll()
    }

    override fun getDefinition(schemaName: String): TableDefinition {
        return schemaRepository.findBySchemaName(schemaName)?.table
            ?: throw EntityNotFoundException("Schema not found: $schemaName")
    }
}