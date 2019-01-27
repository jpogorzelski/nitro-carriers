package io.pogorzelski.nitro.carriers.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.pogorzelski.nitro.carriers.service.RatingService;
import io.pogorzelski.nitro.carriers.service.dto.RatingDTO;
import io.pogorzelski.nitro.carriers.web.rest.errors.BadRequestAlertException;
import io.pogorzelski.nitro.carriers.web.rest.util.HeaderUtil;
import io.pogorzelski.nitro.carriers.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Rating.
 */
@RestController
@RequestMapping("/api/ext")
public class RatingResourceExt {

    private final Logger log = LoggerFactory.getLogger(RatingResourceExt.class);

    private static final String ENTITY_NAME = "rating";

    private final RatingService ratingService;

    public RatingResourceExt(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * POST  /ratings : Create a new rating.
     *
     * @param ratingDTO the ratingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ratingDTO, or with status 400 (Bad Request) if the rating has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ratings")
    @Timed
    public ResponseEntity<RatingDTO> createRating(@Valid @RequestBody RatingDTO ratingDTO) throws URISyntaxException {
        log.debug("REST request to save Rating : {}", ratingDTO);
        if (ratingDTO.getId() != null) {
            throw new BadRequestAlertException("A new rating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RatingDTO result = ratingService.save(ratingDTO);
        return ResponseEntity.created(new URI("/api/ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ratings : Updates an existing rating.
     *
     * @param ratingDTO the ratingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ratingDTO,
     * or with status 400 (Bad Request) if the ratingDTO is not valid,
     * or with status 500 (Internal Server Error) if the ratingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ratings")
    @Timed
    public ResponseEntity<RatingDTO> updateRating(@Valid @RequestBody RatingDTO ratingDTO) throws URISyntaxException {
        log.debug("REST request to update Rating : {}", ratingDTO);
        if (ratingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RatingDTO result = ratingService.save(ratingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ratingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ratings : get all the ratings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ratings in body
     */
    @GetMapping("/ratings")
    @Timed
    public ResponseEntity<List<RatingDTO>> getAllRatings(Pageable pageable) {
        log.debug("REST request to get a page of Ratings");
        Page<RatingDTO> page = ratingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ratings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /ratings/:id : get the "id" rating.
     *
     * @param id the id of the ratingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ratingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ratings/{id}")
    @Timed
    public ResponseEntity<RatingDTO> getRating(@PathVariable Long id) {
        log.debug("REST request to get Rating : {}", id);
        Optional<RatingDTO> ratingDTO = ratingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ratingDTO);
    }

    /**
     * DELETE  /ratings/:id : delete the "id" rating.
     *
     * @param id the id of the ratingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ratings/{id}")
    @Timed
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        log.debug("REST request to delete Rating : {}", id);
        ratingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
