package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.City;
import io.pogorzelski.nitro.carriers.repository.CityRepository;
import io.pogorzelski.nitro.carriers.repository.search.CitySearchRepository;
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
 * Service Implementation for managing City.
 */
@Service
@Transactional
public class CityService {

    private final Logger log = LoggerFactory.getLogger(CityService.class);

    private final CityRepository cityRepository;

    private final CitySearchRepository citySearchRepository;

    public CityService(CityRepository cityRepository, CitySearchRepository citySearchRepository) {
        this.cityRepository = cityRepository;
        this.citySearchRepository = citySearchRepository;
    }

    /**
     * Save a city.
     *
     * @param city the entity to save
     * @return the persisted entity
     */
    public City save(City city) {
        log.debug("Request to save City : {}", city);
        City result = cityRepository.save(city);
        citySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the cities.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<City> findAll() {
        log.debug("Request to get all Cities");
        return cityRepository.findAll();
    }


    /**
     * Get one city by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<City> findOne(Long id) {
        log.debug("Request to get City : {}", id);
        return cityRepository.findById(id);
    }

    /**
     * Delete the city by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete City : {}", id);        cityRepository.deleteById(id);
        citySearchRepository.deleteById(id);
    }

    /**
     * Search for the city corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<City> search(String query) {
        log.debug("Request to search Cities for query {}", query);
        return StreamSupport
            .stream(citySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
