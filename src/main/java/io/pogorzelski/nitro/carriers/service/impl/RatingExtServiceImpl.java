package io.pogorzelski.nitro.carriers.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pogorzelski.nitro.carriers.domain.Rating;
import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.service.RatingExtService;
import io.pogorzelski.nitro.carriers.service.dto.RatingExtDTO;
import io.pogorzelski.nitro.carriers.service.mapper.RatingExtMapper;

/**
 * Service Implementation for managing Rating.
 */
@Service
@Transactional
public class RatingExtServiceImpl implements RatingExtService {

    private final Logger log = LoggerFactory.getLogger(RatingExtServiceImpl.class);

    private final RatingRepository ratingRepository;

    private final RatingExtMapper ratingExtMapper;

    public RatingExtServiceImpl(RatingRepository ratingRepository, RatingExtMapper ratingExtMapper) {
        this.ratingRepository = ratingRepository;
        this.ratingExtMapper = ratingExtMapper;
    }

    /**
     * Save a rating.
     *
     * @param ratingExtDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RatingExtDTO save(RatingExtDTO ratingExtDTO) {
        log.debug("Request to save Rating : {}", ratingExtDTO);

        Rating rating = ratingExtMapper.toEntity(ratingExtDTO);
        rating = ratingRepository.save(rating);
        return ratingExtMapper.toDto(rating);
    }

    /**
     * Get all the ratings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RatingExtDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ratings");
        return ratingRepository.findAll(pageable)
            .map(ratingExtMapper::toDto);
    }


    /**
     * Get one rating by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RatingExtDTO> findOne(Long id) {
        log.debug("Request to get Rating : {}", id);
        return ratingRepository.findById(id)
            .map(ratingExtMapper::toDto);
    }

    /**
     * Delete the rating by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rating : {}", id);
        ratingRepository.deleteById(id);
    }
}
