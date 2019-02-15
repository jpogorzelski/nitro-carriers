package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.repository.CountryRepository;
import io.pogorzelski.nitro.carriers.repository.search.CountrySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Country.
 */
@Service
@Transactional
public class CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryService.class);

    private final CountryRepository countryRepository;

    private final CountrySearchRepository countrySearchRepository;

    public CountryService(CountryRepository countryRepository, CountrySearchRepository countrySearchRepository) {
        this.countryRepository = countryRepository;
        this.countrySearchRepository = countrySearchRepository;
    }

    /**
     * Save a country.
     *
     * @param country the entity to save
     * @return the persisted entity
     */
    public Country save(Country country) {
        log.debug("Request to save Country : {}", country);
        Country result = countryRepository.save(country);
        countrySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the countries.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Country> findAll() {
        log.debug("Request to get all Countries");
        return countryRepository.findAll();
    }


    /**
     * Get one country by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Country> findOne(Long id) {
        log.debug("Request to get Country : {}", id);
        return countryRepository.findById(id);
    }

    /**
     * Delete the country by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Country : {}", id);        countryRepository.deleteById(id);
        countrySearchRepository.deleteById(id);
    }

    /**
     * Search for the country corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Country> search(String query) {
        log.debug("Request to search Countries for query {}", query);
        return StreamSupport
            .stream(countrySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
