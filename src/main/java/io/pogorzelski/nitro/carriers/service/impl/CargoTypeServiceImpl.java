package io.pogorzelski.nitro.carriers.service.impl;

import io.pogorzelski.nitro.carriers.service.CargoTypeService;
import io.pogorzelski.nitro.carriers.domain.CargoType;
import io.pogorzelski.nitro.carriers.repository.CargoTypeRepository;
import io.pogorzelski.nitro.carriers.repository.search.CargoTypeSearchRepository;
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
 * Service Implementation for managing CargoType.
 */
@Service
@Transactional
public class CargoTypeServiceImpl implements CargoTypeService {

    private final Logger log = LoggerFactory.getLogger(CargoTypeServiceImpl.class);

    private final CargoTypeRepository cargoTypeRepository;

    private final CargoTypeSearchRepository cargoTypeSearchRepository;

    public CargoTypeServiceImpl(CargoTypeRepository cargoTypeRepository, CargoTypeSearchRepository cargoTypeSearchRepository) {
        this.cargoTypeRepository = cargoTypeRepository;
        this.cargoTypeSearchRepository = cargoTypeSearchRepository;
    }

    /**
     * Save a cargoType.
     *
     * @param cargoType the entity to save
     * @return the persisted entity
     */
    @Override
    public CargoType save(CargoType cargoType) {
        log.debug("Request to save CargoType : {}", cargoType);
        CargoType result = cargoTypeRepository.save(cargoType);
        cargoTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the cargoTypes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CargoType> findAll() {
        log.debug("Request to get all CargoTypes");
        return cargoTypeRepository.findAll();
    }


    /**
     * Get one cargoType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CargoType> findOne(Long id) {
        log.debug("Request to get CargoType : {}", id);
        return cargoTypeRepository.findById(id);
    }

    /**
     * Delete the cargoType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CargoType : {}", id);        cargoTypeRepository.deleteById(id);
        cargoTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the cargoType corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CargoType> search(String query) {
        log.debug("Request to search CargoTypes for query {}", query);
        return StreamSupport
            .stream(cargoTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
