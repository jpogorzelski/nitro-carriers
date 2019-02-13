package io.pogorzelski.nitro.carriers.service.impl;

import io.pogorzelski.nitro.carriers.domain.Rating;
import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepository;
import io.pogorzelski.nitro.carriers.service.RatingExtService;
import io.pogorzelski.nitro.carriers.service.dto.RatingExtDTO;
import io.pogorzelski.nitro.carriers.service.mapper.RatingExtMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Rating.
 */
@Service
@Transactional
public class RatingExtServiceImpl implements RatingExtService {

    private final Logger log = LoggerFactory.getLogger(RatingExtServiceImpl.class);

    private final RatingRepository ratingRepository;

    private final RatingExtMapper ratingExtMapper;

    private final RatingSearchRepository ratingSearchRepository;

    public RatingExtServiceImpl(RatingRepository ratingRepository, RatingExtMapper ratingExtMapper, RatingSearchRepository ratingSearchRepository) {
        this.ratingRepository = ratingRepository;
        this.ratingExtMapper = ratingExtMapper;
        this.ratingSearchRepository = ratingSearchRepository;
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
        RatingExtDTO result = ratingExtMapper.toDto(rating);
        ratingSearchRepository.save(rating);
        return result;
    }

}
