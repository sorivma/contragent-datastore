package org.akm.contragentdatastore.core.service.files.impl

import org.akm.contragentdatastore.data.schemaindex.entity.Organization
import org.akm.contragentdatastore.api.rest.dto.OrganizationDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.akm.contragentdatastore.api.rest.dto.PhysicalPersonDto
import org.akm.contragentdatastore.core.service.files.FileImportService
import org.akm.contragentdatastore.core.service.files.ImportStatusService
import org.akm.contragentdatastore.core.service.files.MinioService
import org.akm.contragentdatastore.core.service.files.model.FileFormat
import org.akm.contragentdatastore.core.service.files.model.Status
import org.akm.contragentdatastore.core.service.schema.SchemaService
import org.akm.contragentdatastore.data.clickhouse.impl.MinioCsvImportPipeLine
import org.akm.contragentdatastore.data.schemaindex.entity.ImportStatusEntity
import org.akm.contragentdatastore.data.schemaindex.entity.PhysicalPersonEntity
import org.akm.contragentdatastore.data.schemaindex.repository.OrganisationRepository
import org.akm.contragentdatastore.data.schemaindex.repository.PersonRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.InputStream
import java.time.LocalDateTime

@Service
class FileImportServiceImpl(
    @Qualifier("minio-import-datastore-csv")
    private val csvImportPipeLine: MinioCsvImportPipeLine,
    private val schemaService: SchemaService,
    private val importStatusService: ImportStatusService,
    private val minioService: MinioService,
    private val organizationRepository: OrganisationRepository,
    private val personRepository: PersonRepository
) : FileImportService {
    private val logger: Logger = LoggerFactory.getLogger(FileImportServiceImpl::class.java)

    override fun importFile(fileName: String, schemaName: String, format: FileFormat): ImportStatusEntity {
        return when (format) {
            FileFormat.CSV -> {
                val status = importStatusService.createImportStatus()
                csvImportPipeLine.executePipeline(
                    fileName, schemaService.getDefinition(schemaName), schemaName,
                    onError = {
                        logger.error("Error in csv import: {}", it)
                        status.status = Status.FAILED
                        status.message = it
                        status.finishedAt = LocalDateTime.now()
                        importStatusService.updateImportStatus(status)
                    },
                    onComplete = {
                        logger.info("Successfully imported: {}", fileName)
                        status.status = Status.COMPLETED
                        status.finishedAt = LocalDateTime.now()
                        importStatusService.updateImportStatus(status)
                    })
                status
            }
        }
    }

    override fun importOrganzations(importStatuses: ImportStatusEntity): ImportStatusEntity {
        Thread {
            handleOrganizationImport(
                onComplete = {
                    logger.info("Successfully imported organizations: {}", importStatuses)
                    importStatuses.status = Status.COMPLETED
                    importStatuses.finishedAt = LocalDateTime.now()
                    importStatusService.updateImportStatus(importStatuses)
                },
                onError = { message ->
                    logger.error("Error in importing organizations: {}", message)
                    importStatuses.status = Status.FAILED
                    importStatuses.message = message
                    importStatuses.finishedAt = LocalDateTime.now()
                    importStatusService.updateImportStatus(importStatuses)
                }
            )
        }.start()

        return importStatuses
    }

    override fun importPersons(importStatuses: ImportStatusEntity): ImportStatusEntity {
        Thread {
            handlePersonsImport(
                onComplete = {
                    logger.info("Successfully imported persons: {}", importStatuses)
                    importStatuses.status = Status.COMPLETED
                    importStatuses.finishedAt = LocalDateTime.now()
                    importStatusService.updateImportStatus(importStatuses)
                },
                onError = { message ->
                    logger.error("Error in importing persons: {}", message)
                    importStatuses.status = Status.FAILED
                    importStatuses.message = message
                    importStatuses.finishedAt = LocalDateTime.now()
                    importStatusService.updateImportStatus(importStatuses)
                }
            )
        }.start()

        return importStatuses
    }

    @Async
    fun handleOrganizationImport(onComplete: () -> Unit, onError: (String) -> Unit) {
        val inputStreams = minioService.getObjects("contr-agent-bucket", "organizations/")

        for (inputStream in inputStreams) {
            try {
                val organizations = parseOrganizations(inputStream)
                organizationRepository.saveAll(organizations)
            } catch (e: Exception) {
                println("Error processing file: ${e.message}")
                onError("Error processing file: ${e.message}")
            } finally {
                inputStream.close()
            }
        }

        onComplete()
    }

    @Async
    fun handlePersonsImport(onComplete: () -> Unit, onError: (String) -> Unit) {
        val inputStreams = minioService.getObjects("contr-agent-bucket", "persons/")

        for (inputStream in inputStreams) {
            try {
                val persons = parsePersons(inputStream)
                personRepository.saveAll(persons)
            } catch (e: Exception) {
                println("Error processing file: ${e.message}")
                onError("Error processing file: ${e.message}")
                continue
            } finally {
                inputStream.close()
            }
        }

        onComplete()
    }

    private fun parseOrganizations(inputStream: InputStream): List<Organization> {
        val organizations = mutableListOf<Organization>()
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val content = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = ObjectMapper().readTree(content)

        for (jsonNode in jsonArray) {
            val data = jsonNode.get("data") ?: continue

            val organizationDto: OrganizationDto = objectMapper.treeToValue(data, OrganizationDto::class.java)

            organizationDto.inn ?: continue
            organizationDto.kpp ?: continue
            organizationDto.ogrn ?: continue

            organizations.add(
                Organization(
                    inn = organizationDto.inn,
                    kpp = organizationDto.kpp,
                    ogrn = organizationDto.ogrn,
                    kodOpf = organizationDto.opfCode,
                    sprOpf = organizationDto.opfType,
                    dataVyp = organizationDto.issuanceDate,
                    svObrYul = organizationDto.creationInfo,
                    svNaimYul = organizationDto.nameInfo,
                    svRegOrg = organizationDto.registrationAuthority,
                    svAdresYul = organizationDto.addressInfo
                )
            )
        }

        return organizations
    }


    private fun parsePersons(inputStream: InputStream): List<PhysicalPersonEntity> {
        val persons = mutableListOf<PhysicalPersonEntity>()

        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val content = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = ObjectMapper().readTree(content)
        for (jsonNode in jsonArray) {
            val data = jsonNode.get("data") ?: continue

            val personDto = objectMapper.treeToValue(data, PhysicalPersonDto::class.java)

            persons.add(
                PhysicalPersonEntity(
                    inn = personDto.inn,
                    ogrnip = personDto.ogrnip,
                    issueDate = personDto.issueDate,
                    citizenshipInfo = personDto.citizenshipInfo,
                    registrationInfo = personDto.registrationInfo,
                    activityTypeCode = personDto.activityTypeCode,
                    registrationAuthority = personDto.registrationAuthority,
                    activityTypeName = personDto.activityTypeName,
                    terminationInfo = personDto.terminationInfo,
                    ogrnipDate = personDto.ogrnipDate,
                    egipRecords = personDto.egipRecords,
                    individualInfo = personDto.individualInfo
                )
            )
        }

        return persons
    }
}