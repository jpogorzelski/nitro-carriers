package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.domain.Person;
import io.pogorzelski.nitro.carriers.domain.Rating;
import io.pogorzelski.nitro.carriers.repository.CarrierRepository;
import io.pogorzelski.nitro.carriers.repository.CountryRepository;
import io.pogorzelski.nitro.carriers.repository.PersonRepository;
import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.repository.search.CarrierSearchRepository;
import io.pogorzelski.nitro.carriers.repository.search.PersonSearchRepository;
import io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Rating.
 */
@Service
@Transactional
public class RatingExtService {

    private final Logger log = LoggerFactory.getLogger(RatingExtService.class);

    private final RatingRepository ratingRepository;

    private final RatingSearchRepository ratingSearchRepository;
    private final PersonSearchRepository personSearchRepository;
    private final CarrierSearchRepository carrierSearchRepository;

    private final CountryRepository countryRepository;
    private final CarrierRepository carrierRepository;
    private final PersonRepository personRepository;
    private final UserService userService;

    public RatingExtService(CountryRepository countryRepository, CarrierRepository carrierRepository, PersonRepository personRepository, RatingRepository ratingRepository, RatingSearchRepository ratingSearchRepository, PersonSearchRepository personSearchRepository, CarrierSearchRepository carrierSearchRepository, UserService userService) {
        this.ratingRepository = ratingRepository;
        this.ratingSearchRepository = ratingSearchRepository;

        this.countryRepository = countryRepository;
        this.carrierRepository = carrierRepository;
        this.personRepository = personRepository;
        this.personSearchRepository = personSearchRepository;
        this.carrierSearchRepository = carrierSearchRepository;
        this.userService = userService;
    }

    /**
     * Save a rating.
     *
     * @param rating the entity to save
     * @return the persisted entity
     */
    public Rating save(Rating rating) {
        log.debug("Request to save Rating : {}", rating);
        if (rating.getId() != null) {
            checkPermissions(rating.getId());
        }

        Integer carrierTransId = rating.getCarrier().getTransId();
        Carrier carrier = carrierRepository.findByTransId(carrierTransId);
        if (carrier != null) {
            rating.setCarrier(carrier);
        }

        Integer companyId = rating.getPerson().getCompanyId();
        Person person = personRepository.findByCarrier_TransIdAndCompanyId(carrierTransId, companyId);
        if (person != null) {
            rating.setPerson(person);
        }

        String chargeAddressCountry = rating.getChargeCountry().getCountryNamePL();
        Country chargeCountry = countryRepository.findByCountryNamePL(chargeAddressCountry);
        rating.setChargeCountry(chargeCountry);
        if (chargeCountry == null) {
            throw new RuntimeException("Charge country cannot be null!");
        }


        String dischargeAddressCountry = rating.getDischargeCountry().getCountryNamePL();
        Country dischargeCountry = countryRepository.findByCountryNamePL(dischargeAddressCountry);
        rating.setDischargeCountry(dischargeCountry);
        if (dischargeCountry == null) {
            throw new RuntimeException("Discharge country cannot be null!");
        }

        rating.setCreatedBy(userService.getUserWithAuthorities().orElseThrow(() -> new RuntimeException("WTF not logged in")));
        Rating result = ratingRepository.save(rating);

        Carrier dbCarrier = result.getCarrier();
        Person dbPerson = result.getPerson();
        dbPerson.setCarrier(dbCarrier);

        ratingSearchRepository.save(rating);
        carrierSearchRepository.save(dbCarrier);
        personSearchRepository.save(dbPerson);
        return result;
    }

    public boolean checkPermissions(Long id) {
        return ratingRepository.findByCreatedByIsCurrentUser().stream()
            .map(rating -> rating.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new AccessDeniedException("Not allowed to modify this rating."));
    }

}
