package io.pogorzelski.nitro.carriers.service.impl;

import io.pogorzelski.nitro.carriers.service.CarrierService;
import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.repository.CarrierRepository;
import io.pogorzelski.nitro.carriers.repository.search.CarrierSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Carrier.
 */
@Service
@Transactional
public class CarrierServiceImpl implements CarrierService {

    private final Logger log = LoggerFactory.getLogger(CarrierServiceImpl.class);

    private final CarrierRepository carrierRepository;

    private final CarrierSearchRepository carrierSearchRepository;

    public CarrierServiceImpl(CarrierRepository carrierRepository, CarrierSearchRepository carrierSearchRepository) {
        this.carrierRepository = carrierRepository;
        this.carrierSearchRepository = carrierSearchRepository;
    }

    /**
     * Save a carrier.
     *
     * @param carrier the entity to save
     * @return the persisted entity
     */
    @Override
    public Carrier save(Carrier carrier) {
        log.debug("Request to save Carrier : {}", carrier);
        Carrier result = carrierRepository.save(carrier);
        carrierSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the carriers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Carrier> findAll(Pageable pageable) {
        log.debug("Request to get all Carriers");
        return carrierRepository.findAll(pageable);
    }


    /**
     * Get one carrier by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
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
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Carrier : {}", id);        carrierRepository.deleteById(id);
        carrierSearchRepository.deleteById(id);
    }

    /**
     * Search for the carrier corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Carrier> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Carriers for query {}", query);
        return carrierSearchRepository.search(queryStringQuery(query), pageable);    }
}
