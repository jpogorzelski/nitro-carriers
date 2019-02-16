package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.domain.Person;
import io.pogorzelski.nitro.carriers.domain.Rating;
import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepository;
import io.pogorzelski.nitro.carriers.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Rating.
 */
@Service
@Transactional
public class RatingService {

    private final Logger log = LoggerFactory.getLogger(RatingService.class);

    private final RatingRepository ratingRepository;

    private final RatingSearchRepository ratingSearchRepository;

    private final EntityManager entityManager;

    public RatingService(RatingRepository ratingRepository, RatingSearchRepository ratingSearchRepository, EntityManager entityManager) {
        this.ratingRepository = ratingRepository;
        this.ratingSearchRepository = ratingSearchRepository;
        this.entityManager = entityManager;
    }

    /**
     * Save a rating.
     *
     * @param rating the entity to save
     * @return the persisted entity
     */
    public Rating save(Rating rating) {
        log.debug("Request to save Rating : {}", rating);
        if (rating.getId() != null){
            checkPermissions(rating.getId());
        }
        Country chargeCountry = rating.getChargeCountry();
        if (chargeCountry != null && chargeCountry.getId() != null){
            rating.setChargeCountry(entityManager.merge(chargeCountry));
        }
        Country dischargeCountry = rating.getDischargeCountry();
        if (dischargeCountry != null && dischargeCountry.getId() != null){
            rating.setDischargeCountry(entityManager.merge(dischargeCountry));
        }
        Person person = rating.getPerson();
        if (person != null && person.getId() != null){
            rating.setPerson(entityManager.merge(person));
        }
        Carrier carrier = rating.getCarrier();
        if (dischargeCountry != null && carrier.getId() != null){
            rating.setCarrier(entityManager.merge(carrier));
        }

        Rating result = ratingRepository.save(rating);
        ratingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the ratings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Rating> findAll(Pageable pageable) {
        log.debug("Request to get all Ratings");
        return ratingRepository.findAll(pageable);
    }


    /**
     * Get one rating by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Rating> findOne(Long id) {
        log.debug("Request to get Rating : {}", id);
        return ratingRepository.findById(id);
    }

    /**
     * Delete the rating by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Rating : {}", id);
        checkPermissions(id);
        ratingRepository.deleteById(id);
        ratingSearchRepository.deleteById(id);
    }

    /**
     * Search for the rating corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Rating> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ratings for query {}", query);
        return ratingSearchRepository.search(queryStringQuery(query), pageable);    }

    public void checkPermissions(Long id) {
        ratingRepository.findByCreatedByIsCurrentUser().stream()
            .filter(rating -> rating.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new AccessDeniedException("Not allowed to modify this rating."));
    }
}
