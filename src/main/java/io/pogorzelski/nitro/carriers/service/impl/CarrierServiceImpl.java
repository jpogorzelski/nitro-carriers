package io.pogorzelski.nitro.carriers.service.impl;

import io.pogorzelski.nitro.carriers.service.CarrierService;
import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.repository.CarrierRepository;
import io.pogorzelski.nitro.carriers.service.dto.CarrierDTO;
import io.pogorzelski.nitro.carriers.service.mapper.CarrierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Carrier.
 */
@Service
@Transactional
public class CarrierServiceImpl implements CarrierService {

    private final Logger log = LoggerFactory.getLogger(CarrierServiceImpl.class);

    private final CarrierRepository carrierRepository;

    private final CarrierMapper carrierMapper;

    public CarrierServiceImpl(CarrierRepository carrierRepository, CarrierMapper carrierMapper) {
        this.carrierRepository = carrierRepository;
        this.carrierMapper = carrierMapper;
    }

    /**
     * Save a carrier.
     *
     * @param carrierDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CarrierDTO save(CarrierDTO carrierDTO) {
        log.debug("Request to save Carrier : {}", carrierDTO);
        Carrier carrier = carrierMapper.toEntity(carrierDTO);
        carrier = carrierRepository.save(carrier);
        return carrierMapper.toDto(carrier);
    }

    /**
     * Get all the carriers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CarrierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Carriers");
        return carrierRepository.findAll(pageable)
            .map(carrierMapper::toDto);
    }


    /**
     * Get one carrier by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CarrierDTO> findOne(Long id) {
        log.debug("Request to get Carrier : {}", id);
        return carrierRepository.findById(id)
            .map(carrierMapper::toDto);
    }

    /**
     * Delete the carrier by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Carrier : {}", id);        carrierRepository.deleteById(id);
    }
}
