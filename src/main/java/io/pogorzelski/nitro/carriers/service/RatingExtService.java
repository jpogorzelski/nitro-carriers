package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.repository.*;
import io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Rating.
 */
@Service
public class RatingExtService {

    private final Logger log = LoggerFactory.getLogger(RatingExtService.class);

    private final RatingRepository ratingRepository;

    private final RatingSearchRepository ratingSearchRepository;

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final CityService cityService;
    private final CarrierRepository carrierRepository;
    private final PersonRepository personRepository;
    private UserService userService;

    public RatingExtService(CountryRepository countryRepository, CarrierRepository carrierRepository, PersonRepository personRepository, RatingRepository ratingRepository, RatingSearchRepository ratingSearchRepository, final CityRepository cityRepository, CityService cityService, UserService userService) {
        this.ratingRepository = ratingRepository;
        this.ratingSearchRepository = ratingSearchRepository;

        this.countryRepository = countryRepository;
        this.carrierRepository = carrierRepository;
        this.personRepository = personRepository;
        this.cityRepository = cityRepository;
        this.cityService = cityService;
        this.userService = userService;
    }

    /**
     * Save a rating.
     *
     * @param rating the entity to save
     * @return the persisted entity
     */
    @Transactional
    public Rating save(Rating rating) {
        log.debug("Request to save Rating : {}", rating);
        if (rating.getId() != null) {
            checkPermissions(rating.getId());
        }

        String chargeAddressCountry = rating.getChargeCountry().getCountryNamePL();
        Country chargeCountry = countryRepository.findByCountryNamePL(chargeAddressCountry);
        rating.setChargeCountry(chargeCountry);
        if (chargeCountry == null) {
            throw new RuntimeException("Charge country cannot be null!");
        }

        String chargeAddressCity = rating.getChargeCity().getCityName();
        City chargeCity = cityRepository.findByCityName(chargeAddressCity);
        if (chargeCity == null) {
            City chargeCityDTO = rating.getChargeCity();
            chargeCityDTO.setCountry(chargeCountry);
            chargeCity = cityService.save(chargeCityDTO);
            cityRepository.flush();
        }
        rating.setChargeCity(chargeCity);

        String dischargeAddressCountry = rating.getDischargeCountry().getCountryNamePL();
        Country dischargeCountry = countryRepository.findByCountryNamePL(dischargeAddressCountry);
        rating.setDischargeCountry(dischargeCountry);
        if (dischargeCountry == null) {
            throw new RuntimeException("Discharge country cannot be null!");
        }

        String dischargeAddressCity = rating.getDischargeCity().getCityName();
        City dischargeCity = cityRepository.findByCityName(dischargeAddressCity);
        if (dischargeCity == null) {
            City dischargeCityDTO = rating.getDischargeCity();
            dischargeCityDTO.setCountry(dischargeCountry);
            dischargeCity = cityService.save(dischargeCityDTO);
            cityRepository.flush();
        }
        rating.setDischargeCity(dischargeCity);

        rating.setCreatedBy(getUser());

        Integer carrierTransId = rating.getCarrier().getTransId();
        Carrier carrier = carrierRepository.findByTransId(carrierTransId);
        if (carrier == null) {
            Carrier carrierDTO = rating.getCarrier();
            carrier = carrierRepository.saveAndFlush(carrierDTO);
        }

        Integer personCompanyId = rating.getPerson().getCompanyId();
        Person person = personRepository.findByCarrier_TransIdAndCompanyId(carrierTransId, personCompanyId);
        if (person == null) {
            Person personDTO = rating.getPerson();
            person = personRepository.saveAndFlush(personDTO);
        }
        person.setCarrier(carrier);

        rating.setCarrier(carrier);
        rating.setPerson(person);

        if (rating.isAddAlternative()) {
            Carrier altCarrierDTO = rating.getAltCarrier();
            Integer altCarrierTransId = altCarrierDTO.getTransId();
            Carrier altCarrier = carrierRepository.findByTransId(altCarrierTransId);
            if (altCarrier == null) {
                altCarrier = carrierRepository.saveAndFlush(altCarrierDTO);
            }

            Person altPersonDTO = rating.getAltPerson();
            Integer altPersonCompanyId = altPersonDTO.getCompanyId();
            Person altPerson = personRepository.findByCarrier_TransIdAndCompanyId(altCarrierTransId, altPersonCompanyId);
            if (altPerson == null) {
                altPerson = personRepository.saveAndFlush(altPersonDTO);
                altPerson.setCarrier(altCarrier);
            }

            rating.setAltCarrier(altCarrier);
            rating.setAltPerson(altPerson);
        } else {
            rating.setAltCarrier(null);
            rating.setAltPerson(null);
        }

        Rating result = ratingRepository.save(rating);
        ratingSearchRepository.save(rating);
        return result;
    }

    public User getUser() {
        return userService.getUserWithAuthorities()
            .orElseThrow(() -> new AccessDeniedException("You are not logged in!"));
    }

    /**
     * Delete the rating by id.
     *
     * @param id the id of the entity
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Rating : {}", id);
        checkPermissions(id);
        ratingRepository.deleteById(id);
        ratingSearchRepository.deleteById(id);
    }

    private void checkPermissions(Long id) {
        ratingRepository.findByCreatedByIsCurrentUser().stream()
            .map(rating -> rating.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new AccessDeniedException("Not allowed to modify this rating."));
    }

    @Transactional(readOnly = true)
    public Page<Rating> findCarrierRatings(Long id, Pageable pageable) {
        return ratingRepository.findByCarrier_IdOrAltCarrier_Id(pageable, id, id);

    }
}
