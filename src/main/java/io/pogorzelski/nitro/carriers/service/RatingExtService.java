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
}
