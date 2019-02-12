package io.pogorzelski.nitro.carriers.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.pogorzelski.nitro.carriers.domain.Person;
import io.pogorzelski.nitro.carriers.domain.*; // for static metamodels
import io.pogorzelski.nitro.carriers.repository.PersonRepository;
import io.pogorzelski.nitro.carriers.repository.search.PersonSearchRepository;
import io.pogorzelski.nitro.carriers.service.dto.PersonCriteria;

/**
 * Service for executing complex queries for Person entities in the database.
 * The main input is a {@link PersonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Person} or a {@link Page} of {@link Person} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonQueryService extends QueryService<Person> {

    private final Logger log = LoggerFactory.getLogger(PersonQueryService.class);

    private final PersonRepository personRepository;

    private final PersonSearchRepository personSearchRepository;

    public PersonQueryService(PersonRepository personRepository, PersonSearchRepository personSearchRepository) {
        this.personRepository = personRepository;
        this.personSearchRepository = personSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Person} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Person> findByCriteria(PersonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Person} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Person> findByCriteria(PersonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.count(specification);
    }

    /**
     * Function to convert PersonCriteria to a {@link Specification}
     */
    private Specification<Person> createSpecification(PersonCriteria criteria) {
        Specification<Person> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Person_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Person_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Person_.lastName));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompanyId(), Person_.companyId));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Person_.phoneNumber));
            }
            if (criteria.getCarrierId() != null) {
                specification = specification.and(buildSpecification(criteria.getCarrierId(),
                    root -> root.join(Person_.carrier, JoinType.LEFT).get(Carrier_.id)));
            }
        }
        return specification;
    }
}
