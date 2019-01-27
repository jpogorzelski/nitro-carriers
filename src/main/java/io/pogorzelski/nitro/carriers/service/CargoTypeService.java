package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.service.dto.CargoTypeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing CargoType.
 */
public interface CargoTypeService {

    /**
     * Save a cargoType.
     *
     * @param cargoTypeDTO the entity to save
     * @return the persisted entity
     */
    CargoTypeDTO save(CargoTypeDTO cargoTypeDTO);

    /**
     * Get all the cargoTypes.
     *
     * @return the list of entities
     */
    List<CargoTypeDTO> findAll();


    /**
     * Get the "id" cargoType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CargoTypeDTO> findOne(Long id);

    /**
     * Delete the "id" cargoType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
