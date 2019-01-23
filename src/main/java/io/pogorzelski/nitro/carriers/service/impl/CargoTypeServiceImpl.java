package io.pogorzelski.nitro.carriers.service.impl;

import io.pogorzelski.nitro.carriers.service.CargoTypeService;
import io.pogorzelski.nitro.carriers.domain.CargoType;
import io.pogorzelski.nitro.carriers.repository.CargoTypeRepository;
import io.pogorzelski.nitro.carriers.service.dto.CargoTypeDTO;
import io.pogorzelski.nitro.carriers.service.mapper.CargoTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CargoType.
 */
@Service
@Transactional
public class CargoTypeServiceImpl implements CargoTypeService {

    private final Logger log = LoggerFactory.getLogger(CargoTypeServiceImpl.class);

    private final CargoTypeRepository cargoTypeRepository;

    private final CargoTypeMapper cargoTypeMapper;

    public CargoTypeServiceImpl(CargoTypeRepository cargoTypeRepository, CargoTypeMapper cargoTypeMapper) {
        this.cargoTypeRepository = cargoTypeRepository;
        this.cargoTypeMapper = cargoTypeMapper;
    }

    /**
     * Save a cargoType.
     *
     * @param cargoTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CargoTypeDTO save(CargoTypeDTO cargoTypeDTO) {
        log.debug("Request to save CargoType : {}", cargoTypeDTO);

        CargoType cargoType = cargoTypeMapper.toEntity(cargoTypeDTO);
        cargoType = cargoTypeRepository.save(cargoType);
        return cargoTypeMapper.toDto(cargoType);
    }

    /**
     * Get all the cargoTypes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CargoTypeDTO> findAll() {
        log.debug("Request to get all CargoTypes");
        return cargoTypeRepository.findAll().stream()
            .map(cargoTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one cargoType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CargoTypeDTO> findOne(Long id) {
        log.debug("Request to get CargoType : {}", id);
        return cargoTypeRepository.findById(id)
            .map(cargoTypeMapper::toDto);
    }

    /**
     * Delete the cargoType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CargoType : {}", id);
        cargoTypeRepository.deleteById(id);
    }
}
