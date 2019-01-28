package io.pogorzelski.nitro.carriers.service.impl;

import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.repository.AddressRepository;
import io.pogorzelski.nitro.carriers.repository.CarrierRepository;
import io.pogorzelski.nitro.carriers.repository.PersonRepository;
import io.pogorzelski.nitro.carriers.service.AddressService;
import io.pogorzelski.nitro.carriers.service.PersonService;
import io.pogorzelski.nitro.carriers.service.dto.CarrierDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.service.RatingExtService;
import io.pogorzelski.nitro.carriers.service.dto.RatingExtDTO;
import io.pogorzelski.nitro.carriers.service.mapper.RatingExtMapper;

/**
 * Service Implementation for managing Rating.
 */
@Service
@Transactional
public class RatingExtServiceImpl implements RatingExtService {

    private final Logger log = LoggerFactory.getLogger(RatingExtServiceImpl.class);

    private final RatingRepository ratingRepository;
    private final RatingExtMapper ratingExtMapper;

    private final CarrierRepository carrierRepository;
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    public RatingExtServiceImpl(RatingRepository ratingRepository, RatingExtMapper ratingExtMapper, CarrierRepository carrierRepository, PersonRepository personRepository, AddressRepository addressRepository) {
        this.ratingRepository = ratingRepository;
        this.ratingExtMapper = ratingExtMapper;
        this.carrierRepository = carrierRepository;
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * Save a rating.
     *
     * @param ratingExtDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RatingExtDTO save(RatingExtDTO ratingExtDTO) {
        log.debug("Request to save Rating : {}", ratingExtDTO);
/*
        Carrier carrier = new Carrier();
        carrier.setName(ratingExtDTO.getCarrierName());
        carrier.setTransId(ratingExtDTO.getCarrierTransId());

        Person person = new Person();
        person.setCarrier(carrier);
        person.setCompanyId(ratingExtDTO.getCarrierTransId());
        person.setFirstName(ratingExtDTO.getPersonFirstName());
        person.setLastName(ratingExtDTO.getPersonLastName());

        Country chargeCountry = new Country();
        chargeCountry.setCountryName(ratingExtDTO.getChargeAddressCountry());
        Address chargeAddress = new Address();
        chargeAddress.setCountry(chargeCountry);
        chargeAddress.setPostalCode(ratingExtDTO.getChargeAddressPostalCode());

        Country dischargeCountry = new Country();
        dischargeCountry.setCountryName(ratingExtDTO.getDischargeAddressCountry());
        Address dischargeAddress = new Address();
        dischargeAddress.setCountry(dischargeCountry);
        dischargeAddress.setPostalCode(ratingExtDTO.getDischargeAddressPostalCode());*/
        /*
        carrierRepository.save(carrier);
        personRepository.save(person);
        addressRepository.save(chargeAddress);
        addressRepository.save(dischargeAddress);
*/
        Rating rating = ratingExtMapper.toEntity(ratingExtDTO);
        rating = ratingRepository.save(rating);
        return ratingExtMapper.toDto(rating);
    }

}
