package io.pogorzelski.nitro.carriers.web.rest;
import io.pogorzelski.nitro.carriers.domain.CargoType;
import io.pogorzelski.nitro.carriers.service.CargoTypeService;
import io.pogorzelski.nitro.carriers.web.rest.errors.BadRequestAlertException;
import io.pogorzelski.nitro.carriers.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing CargoType.
 */
@RestController
@RequestMapping("/api")
public class CargoTypeResource {

    private final Logger log = LoggerFactory.getLogger(CargoTypeResource.class);

    private static final String ENTITY_NAME = "cargoType";

    private final CargoTypeService cargoTypeService;

    public CargoTypeResource(CargoTypeService cargoTypeService) {
        this.cargoTypeService = cargoTypeService;
    }

    /**
     * POST  /cargo-types : Create a new cargoType.
     *
     * @param cargoType the cargoType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cargoType, or with status 400 (Bad Request) if the cargoType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cargo-types")
    public ResponseEntity<CargoType> createCargoType(@Valid @RequestBody CargoType cargoType) throws URISyntaxException {
        log.debug("REST request to save CargoType : {}", cargoType);
        if (cargoType.getId() != null) {
            throw new BadRequestAlertException("A new cargoType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CargoType result = cargoTypeService.save(cargoType);
        return ResponseEntity.created(new URI("/api/cargo-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cargo-types : Updates an existing cargoType.
     *
     * @param cargoType the cargoType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cargoType,
     * or with status 400 (Bad Request) if the cargoType is not valid,
     * or with status 500 (Internal Server Error) if the cargoType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cargo-types")
    public ResponseEntity<CargoType> updateCargoType(@Valid @RequestBody CargoType cargoType) throws URISyntaxException {
        log.debug("REST request to update CargoType : {}", cargoType);
        if (cargoType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CargoType result = cargoTypeService.save(cargoType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cargoType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cargo-types : get all the cargoTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cargoTypes in body
     */
    @GetMapping("/cargo-types")
    public List<CargoType> getAllCargoTypes() {
        log.debug("REST request to get all CargoTypes");
        return cargoTypeService.findAll();
    }

    /**
     * GET  /cargo-types/:id : get the "id" cargoType.
     *
     * @param id the id of the cargoType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cargoType, or with status 404 (Not Found)
     */
    @GetMapping("/cargo-types/{id}")
    public ResponseEntity<CargoType> getCargoType(@PathVariable Long id) {
        log.debug("REST request to get CargoType : {}", id);
        Optional<CargoType> cargoType = cargoTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cargoType);
    }

    /**
     * DELETE  /cargo-types/:id : delete the "id" cargoType.
     *
     * @param id the id of the cargoType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cargo-types/{id}")
    public ResponseEntity<Void> deleteCargoType(@PathVariable Long id) {
        log.debug("REST request to delete CargoType : {}", id);
        cargoTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cargo-types?query=:query : search for the cargoType corresponding
     * to the query.
     *
     * @param query the query of the cargoType search
     * @return the result of the search
     */
    @GetMapping("/_search/cargo-types")
    public List<CargoType> searchCargoTypes(@RequestParam String query) {
        log.debug("REST request to search CargoTypes for query {}", query);
        return cargoTypeService.search(query);
    }

}
