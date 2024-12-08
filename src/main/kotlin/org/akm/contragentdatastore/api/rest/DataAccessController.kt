package org.akm.contragentdatastore.api.rest

import jakarta.persistence.EntityNotFoundException
import org.akm.contragentdatastore.core.service.files.DataAccessService
import org.akm.contragentdatastore.data.schemaindex.entity.Organization
import org.akm.contragentdatastore.data.schemaindex.entity.PhysicalPersonEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("${Routing.APIV1}${Routing.APIv1.DATA}")
class DataAccessController(
    private val dataAccessService: DataAccessService
) {
    @GetMapping(Routing.APIv1.Data.ORGANIZATIONS)
    fun getOrg(@PageableDefault pageable: Pageable): Page<Organization> {
        return dataAccessService.getOrganizations(pageable)
    }

    @GetMapping(Routing.APIv1.Data.PERSONS)
    fun getPer(@PageableDefault pageable: Pageable): Page<PhysicalPersonEntity> {
        return dataAccessService.getPersons(pageable)
    }

    @GetMapping("${Routing.APIv1.Data.ORGANIZATIONS}/{inn}")
    fun getOrgByInn(@PathVariable("inn") inn: String): Organization {
        return dataAccessService.getOrganization(inn)
            ?: throw EntityNotFoundException("No organization found for $inn")
    }

    @GetMapping("${Routing.APIv1.Data.PERSONS}/{inn}")
    fun getPerByInn(@PathVariable("inn") inn: String): PhysicalPersonEntity {
        return dataAccessService.getPerson(inn) ?:
        throw EntityNotFoundException("No person found for $inn")
    }
}