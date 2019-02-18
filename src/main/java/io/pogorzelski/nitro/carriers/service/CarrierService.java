package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.repository.CarrierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Carrier.
 */
@Service
@Transactional
public class CarrierService {

    private final Logger log = LoggerFactory.getLogger(CarrierService.class);

    private final CarrierRepository carrierRepository;

    public CarrierService(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    /**
     * Save a carrier.
     *
     * @param carrier the entity to save
     * @return the persisted entity
     */
    public Carrier save(Carrier carrier) {
        log.debug("Request to save Carrier : {}", carrier);
        return carrierRepository.save(carrier);
    }

    /**
     * Get all the carriers.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Carrier> findAll() {
        log.debug("Request to get all Carriers");
        return carrierRepository.findAll();
    }


    /**
     * Get one carrier by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Carrier> findOne(Long id) {
        log.debug("Request to get Carrier : {}", id);
        return carrierRepository.findById(id);
    }

    /**
     * Delete the carrier by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Carrier : {}", id);
        carrierRepository.deleteById(id);
    }

}
