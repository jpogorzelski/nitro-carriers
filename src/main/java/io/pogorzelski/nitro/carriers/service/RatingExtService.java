package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.Rating;

/**
 * Service Interface for managing Rating.
 */
public interface RatingExtService {

    /**
     * Save a rating.
     *
     * @param rating the entity to save
     * @return the persisted entity
     */
    Rating save(Rating rating);
}
