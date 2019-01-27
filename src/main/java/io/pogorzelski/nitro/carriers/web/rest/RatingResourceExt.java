package io.pogorzelski.nitro.carriers.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pogorzelski.nitro.carriers.service.RatingExtService;
import io.pogorzelski.nitro.carriers.service.dto.RatingExtDTO;
import io.pogorzelski.nitro.carriers.web.rest.errors.BadRequestAlertException;
import io.pogorzelski.nitro.carriers.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Rating.
 */
@RestController
@RequestMapping("/api/ext")
public class RatingResourceExt {

    private final Logger log = LoggerFactory.getLogger(RatingResourceExt.class);

    private static final String ENTITY_NAME = "rating";

    private final RatingExtService ratingExtService;

    public RatingResourceExt(RatingExtService ratingExtService) {
        this.ratingExtService = ratingExtService;
    }

    /**
     * POST  /ratings : Create a new rating.
     *
     * @param ratingExtDTO the ratingExtDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ratingExtDTO, or with status 400 (Bad Request) if the rating has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ratings")
    public ResponseEntity<RatingExtDTO> createRating(@Valid @RequestBody RatingExtDTO ratingExtDTO) throws URISyntaxException {
        log.debug("REST request to save Full Rating : {}", ratingExtDTO);
        if (ratingExtDTO.getId() != null) {
            throw new BadRequestAlertException("A new rating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RatingExtDTO result = ratingExtService.save(ratingExtDTO);
        return ResponseEntity.created(new URI("/api/ext/ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }



}
