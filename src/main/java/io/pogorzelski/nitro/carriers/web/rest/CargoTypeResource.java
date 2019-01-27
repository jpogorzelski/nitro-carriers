package io.pogorzelski.nitro.carriers.web.rest;
import io.pogorzelski.nitro.carriers.service.CargoTypeService;
import io.pogorzelski.nitro.carriers.web.rest.errors.BadRequestAlertException;
import io.pogorzelski.nitro.carriers.web.rest.util.HeaderUtil;
import io.pogorzelski.nitro.carriers.service.dto.CargoTypeDTO;
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
     * @param cargoTypeDTO the cargoTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cargoTypeDTO, or with status 400 (Bad Request) if the cargoType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cargo-types")
    public ResponseEntity<CargoTypeDTO> createCargoType(@Valid @RequestBody CargoTypeDTO cargoTypeDTO) throws URISyntaxException {
        log.debug("REST request to save CargoType : {}", cargoTypeDTO);
        if (cargoTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new cargoType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CargoTypeDTO result = cargoTypeService.save(cargoTypeDTO);
        return ResponseEntity.created(new URI("/api/cargo-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cargo-types : Updates an existing cargoType.
     *
     * @param cargoTypeDTO the cargoTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cargoTypeDTO,
     * or with status 400 (Bad Request) if the cargoTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the cargoTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cargo-types")
    public ResponseEntity<CargoTypeDTO> updateCargoType(@Valid @RequestBody CargoTypeDTO cargoTypeDTO) throws URISyntaxException {
        log.debug("REST request to update CargoType : {}", cargoTypeDTO);
        if (cargoTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CargoTypeDTO result = cargoTypeService.save(cargoTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cargoTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cargo-types : get all the cargoTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cargoTypes in body
     */
    @GetMapping("/cargo-types")
    public List<CargoTypeDTO> getAllCargoTypes() {
        log.debug("REST request to get all CargoTypes");
        return cargoTypeService.findAll();
    }

    /**
     * GET  /cargo-types/:id : get the "id" cargoType.
     *
     * @param id the id of the cargoTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cargoTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cargo-types/{id}")
    public ResponseEntity<CargoTypeDTO> getCargoType(@PathVariable Long id) {
        log.debug("REST request to get CargoType : {}", id);
        Optional<CargoTypeDTO> cargoTypeDTO = cargoTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cargoTypeDTO);
    }

    /**
     * DELETE  /cargo-types/:id : delete the "id" cargoType.
     *
     * @param id the id of the cargoTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cargo-types/{id}")
    public ResponseEntity<Void> deleteCargoType(@PathVariable Long id) {
        log.debug("REST request to delete CargoType : {}", id);
        cargoTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
