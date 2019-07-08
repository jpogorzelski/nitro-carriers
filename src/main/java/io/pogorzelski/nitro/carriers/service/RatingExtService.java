package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.repository.*;
import io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepository;
import io.pogorzelski.nitro.carriers.web.rest.errors.RatingException;
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

        Carrier carrier = getCarrier(rating.getCarrier());
        Person person = getPerson(rating.getPerson(), carrier);

        rating.setCarrier(carrier);
        rating.setPerson(person);

        if (rating.isAddAlternative()) {
            Carrier altCarrier = getCarrier(rating.getAltCarrier());
            Person altPerson = getPerson(rating.getAltPerson(), altCarrier);

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

    private Carrier getCarrier(Carrier carrierDTO) {
        if (carrierDTO == null) {
            throw new RatingException("Carrier cannot be null");
        }
        Integer carrierTransId = carrierDTO.getTransId();
        Carrier carrier = carrierRepository.findByTransId(carrierTransId);
        if (carrier == null) {
            carrier = carrierRepository.saveAndFlush(carrierDTO);
        }
        return carrier;
    }

    private Person getPerson(Person personDTO, Carrier carrier) {
        if (personDTO == null) {
            throw new RatingException("Person cannot be null");
        }
        Integer carrierTransId = carrier.getTransId();
        Integer personCompanyId = personDTO.getCompanyId();
        Person person = personRepository.findByCarrier_TransIdAndCompanyId(carrierTransId, personCompanyId);
        if (person == null) {
            personDTO.setCarrier(carrier);
            person = personRepository.saveAndFlush(personDTO);
        } else {
            if (personDTO.getPhoneNumber() != null) {
                person.setPhoneNumber(personDTO.getPhoneNumber());
            }
        }
        return person;
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
