package io.pogorzelski.nitro.carriers.web.rest;
import io.pogorzelski.nitro.carriers.domain.Rating;
import io.pogorzelski.nitro.carriers.service.RatingService;
import io.pogorzelski.nitro.carriers.web.rest.errors.BadRequestAlertException;
import io.pogorzelski.nitro.carriers.web.rest.util.HeaderUtil;
import io.pogorzelski.nitro.carriers.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Rating.
 */
@RestController
@RequestMapping("/api")
public class RatingResource {

    private final Logger log = LoggerFactory.getLogger(RatingResource.class);

    private static final String ENTITY_NAME = "rating";

    private final RatingService ratingService;

    public RatingResource(RatingService ratingService) {
        this.ratingService = ratingService;
    }



    /**
     * GET  /ratings : get all the ratings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ratings in body
     */
    @GetMapping("/ratings")
    public ResponseEntity<List<Rating>> getAllRatings(Pageable pageable) {
        log.debug("REST request to get a page of Ratings");
        Page<Rating> page = ratingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ratings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /ratings/:id : get the "id" rating.
     *
     * @param id the id of the rating to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rating, or with status 404 (Not Found)
     */
    @GetMapping("/ratings/{id}")
    public ResponseEntity<Rating> getRating(@PathVariable Long id) {
        log.debug("REST request to get Rating : {}", id);
        Optional<Rating> rating = ratingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rating);
    }

    /**
     * SEARCH  /_search/ratings?query=:query : search for the rating corresponding
     * to the query.
     *
     * @param query the query of the rating search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/ratings")
    public ResponseEntity<List<Rating>> searchRatings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Ratings for query {}", query);
        Page<Rating> page = ratingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ratings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
