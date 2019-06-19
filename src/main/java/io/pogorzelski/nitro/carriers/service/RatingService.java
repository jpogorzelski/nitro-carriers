package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.domain.City;
import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.domain.Person;
import io.pogorzelski.nitro.carriers.domain.Rating;
import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Rating.
 */
@Service
@Transactional(readOnly = true)
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
        Country chargeCountry = rating.getChargeCountry();
        if (chargeCountry != null && chargeCountry.getId() != null){
            rating.setChargeCountry(entityManager.merge(chargeCountry));
        }
        Country dischargeCountry = rating.getDischargeCountry();
        if (dischargeCountry != null && dischargeCountry.getId() != null){
            rating.setDischargeCountry(entityManager.merge(dischargeCountry));
        }
        City dischargeCity = rating.getDischargeCity();
        if (dischargeCity != null && dischargeCity.getId() != null){
            rating.setDischargeCity(entityManager.merge(dischargeCity));
        }
        Person person = rating.getPerson();
        if (person != null && person.getId() != null){
            rating.setPerson(entityManager.merge(person));
        }
        Carrier carrier = rating.getCarrier();
        if (dischargeCountry != null && carrier.getId() != null){
            rating.setCarrier(entityManager.merge(carrier));
        }
        City chargeCity = rating.getChargeCity();
        if (chargeCity != null && chargeCity.getId() != null){
            rating.setChargeCity(entityManager.merge(chargeCity));
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
    public Optional<Rating> findOne(Long id) {
        log.debug("Request to get Rating : {}", id);
        return ratingRepository.findById(id);
    }


    /**
     * Search for the rating corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<Rating> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ratings for query {}", query);
        return ratingSearchRepository.search(queryStringQuery(query), pageable);    }
}
