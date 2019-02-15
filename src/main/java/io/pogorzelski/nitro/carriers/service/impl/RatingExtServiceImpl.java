package io.pogorzelski.nitro.carriers.service.impl;

import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.repository.*;
import io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepository;
import io.pogorzelski.nitro.carriers.service.RatingExtService;
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

    private final RatingSearchRepository ratingSearchRepository;

    private final CountryRepository countryRepository;
    private final CarrierRepository carrierRepository;
    private final PersonRepository personRepository;
    private final CargoTypeRepository cargoTypeRepository;


    public RatingExtServiceImpl(CountryRepository countryRepository, CarrierRepository carrierRepository, PersonRepository personRepository, CargoTypeRepository cargoTypeRepository, RatingRepository ratingRepository, RatingSearchRepository ratingSearchRepository) {
        this.ratingRepository = ratingRepository;
        this.ratingSearchRepository = ratingSearchRepository;

        this.countryRepository = countryRepository;
        this.carrierRepository = carrierRepository;
        this.personRepository = personRepository;
        this.cargoTypeRepository = cargoTypeRepository;
    }

    /**
     * Save a rating.
     *
     * @param rating the entity to save
     * @return the persisted entity
     */
    @Override
    public Rating save(Rating rating) {
        log.debug("Request to save Rating : {}", rating);


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

        String chargeAddressCountry = rating.getChargeCountry().getCountryName();
        Country chargeCountry = countryRepository.findByCountryName(chargeAddressCountry);
        rating.setChargeCountry(chargeCountry);
        if (chargeCountry == null) {
            throw new RuntimeException("Charge country cannot be null!");
        }


        String dischargeAddressCountry = rating.getDischargeCountry().getCountryName();
        Country dischargeCountry = countryRepository.findByCountryName(dischargeAddressCountry);
        rating.setDischargeCountry(dischargeCountry);
        if (dischargeCountry == null) {
            throw new RuntimeException("Discharge country cannot be null!");
        }

        Long cargoTypeId = rating.getCargoType().getId();
        CargoType cargoType = cargoTypeRepository.findById(cargoTypeId)
            .orElseThrow(() -> new RuntimeException("Cargo type cannot be null!"));


        Rating result = ratingRepository.save(rating);
        ratingSearchRepository.save(rating);

         result.getPerson().setCarrier(result.getCarrier());
        return result;
    }

}
