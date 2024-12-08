package org.akm.contragentdatastore.core.service.files.impl

import org.akm.contragentdatastore.core.service.files.DataAccessService
import org.akm.contragentdatastore.data.schemaindex.entity.Organization
import org.akm.contragentdatastore.data.schemaindex.entity.PhysicalPersonEntity
import org.akm.contragentdatastore.data.schemaindex.repository.OrganisationRepository
import org.akm.contragentdatastore.data.schemaindex.repository.PersonRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DataAccessServiceImpl(
    private val personRepository: PersonRepository,
    private val organisationRepository: OrganisationRepository
) : DataAccessService {
    override fun getOrganizations(pageable: Pageable): Page<Organization> {
        return organisationRepository.findAll(pageable)
    }

    override fun getPersons(pageable: Pageable): Page<PhysicalPersonEntity> {
        return personRepository.findAll(pageable)
    }

    override fun getPerson(inn: String): PhysicalPersonEntity? {
        return personRepository.findById(inn).orElse(null)
    }

    override fun getOrganization(inn: String): Organization? {
        return organisationRepository.findById(inn).orElse(null)
    }
}