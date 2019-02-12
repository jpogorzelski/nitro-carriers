package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.Person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Person.
 */
public interface PersonService {

    /**
     * Save a person.
     *
     * @param person the entity to save
     * @return the persisted entity
     */
    Person save(Person person);

    /**
     * Get all the people.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Person> findAll(Pageable pageable);


    /**
     * Get the "id" person.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Person> findOne(Long id);

    /**
     * Delete the "id" person.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the person corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Person> search(String query, Pageable pageable);
}
