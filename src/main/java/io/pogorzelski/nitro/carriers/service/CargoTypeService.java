package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.CargoType;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing CargoType.
 */
public interface CargoTypeService {

    /**
     * Save a cargoType.
     *
     * @param cargoType the entity to save
     * @return the persisted entity
     */
    CargoType save(CargoType cargoType);

    /**
     * Get all the cargoTypes.
     *
     * @return the list of entities
     */
    List<CargoType> findAll();


    /**
     * Get the "id" cargoType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CargoType> findOne(Long id);

    /**
     * Delete the "id" cargoType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the cargoType corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<CargoType> search(String query);
}
