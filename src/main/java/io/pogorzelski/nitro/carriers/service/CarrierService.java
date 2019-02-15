package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.repository.CarrierRepository;
import io.pogorzelski.nitro.carriers.repository.search.CarrierSearchRepository;
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
 * Service Implementation for managing Carrier.
 */
@Service
@Transactional
public class CarrierService {

    private final Logger log = LoggerFactory.getLogger(CarrierService.class);

    private final CarrierRepository carrierRepository;

    private final CarrierSearchRepository carrierSearchRepository;

    public CarrierService(CarrierRepository carrierRepository, CarrierSearchRepository carrierSearchRepository) {
        this.carrierRepository = carrierRepository;
        this.carrierSearchRepository = carrierSearchRepository;
    }

    /**
     * Save a carrier.
     *
     * @param carrier the entity to save
     * @return the persisted entity
     */
    public Carrier save(Carrier carrier) {
        log.debug("Request to save Carrier : {}", carrier);
        Carrier result = carrierRepository.save(carrier);
        carrierSearchRepository.save(result);
        return result;
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
        log.debug("Request to delete Carrier : {}", id);        carrierRepository.deleteById(id);
        carrierSearchRepository.deleteById(id);
    }

    /**
     * Search for the carrier corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Carrier> search(String query) {
        log.debug("Request to search Carriers for query {}", query);
        return StreamSupport
            .stream(carrierSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
