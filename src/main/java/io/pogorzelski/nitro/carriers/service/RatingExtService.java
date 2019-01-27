package io.pogorzelski.nitro.carriers.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.pogorzelski.nitro.carriers.service.dto.RatingExtDTO;

/**
 * Service Interface for managing Rating.
 */
public interface RatingExtService {

    /**
     * Save a rating.
     *
     * @param ratingExtDTO the entity to save
     * @return the persisted entity
     */
    RatingExtDTO save(RatingExtDTO ratingExtDTO);

    /**
     * Get all the ratings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RatingExtDTO> findAll(Pageable pageable);


    /**
     * Get the "id" rating.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RatingExtDTO> findOne(Long id);

    /**
     * Delete the "id" rating.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
