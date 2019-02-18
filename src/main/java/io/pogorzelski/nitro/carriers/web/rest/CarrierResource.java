package io.pogorzelski.nitro.carriers.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.service.CarrierService;
import io.pogorzelski.nitro.carriers.web.rest.errors.BadRequestAlertException;
import io.pogorzelski.nitro.carriers.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Carrier.
 */
@RestController
@RequestMapping("/api")
public class CarrierResource {

    private final Logger log = LoggerFactory.getLogger(CarrierResource.class);

    private static final String ENTITY_NAME = "carrier";

    private final CarrierService carrierService;

    public CarrierResource(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    /**
     * POST  /carriers : Create a new carrier.
     *
     * @param carrier the carrier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new carrier, or with status 400 (Bad Request) if the carrier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/carriers")
    public ResponseEntity<Carrier> createCarrier(@Valid @RequestBody Carrier carrier) throws URISyntaxException {
        log.debug("REST request to save Carrier : {}", carrier);
        if (carrier.getId() != null) {
            throw new BadRequestAlertException("A new carrier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Carrier result = carrierService.save(carrier);
        return ResponseEntity.created(new URI("/api/carriers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /carriers : Updates an existing carrier.
     *
     * @param carrier the carrier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated carrier,
     * or with status 400 (Bad Request) if the carrier is not valid,
     * or with status 500 (Internal Server Error) if the carrier couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/carriers")
    public ResponseEntity<Carrier> updateCarrier(@Valid @RequestBody Carrier carrier) throws URISyntaxException {
        log.debug("REST request to update Carrier : {}", carrier);
        if (carrier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Carrier result = carrierService.save(carrier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, carrier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /carriers : get all the carriers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of carriers in body
     */
    @GetMapping("/carriers")
    public List<Carrier> getAllCarriers() {
        log.debug("REST request to get all Carriers");
        return carrierService.findAll();
    }

    /**
     * GET  /carriers/:id : get the "id" carrier.
     *
     * @param id the id of the carrier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the carrier, or with status 404 (Not Found)
     */
    @GetMapping("/carriers/{id}")
    public ResponseEntity<Carrier> getCarrier(@PathVariable Long id) {
        log.debug("REST request to get Carrier : {}", id);
        Optional<Carrier> carrier = carrierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carrier);
    }

    /**
     * DELETE  /carriers/:id : delete the "id" carrier.
     *
     * @param id the id of the carrier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/carriers/{id}")
    public ResponseEntity<Void> deleteCarrier(@PathVariable Long id) {
        log.debug("REST request to delete Carrier : {}", id);
        carrierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
