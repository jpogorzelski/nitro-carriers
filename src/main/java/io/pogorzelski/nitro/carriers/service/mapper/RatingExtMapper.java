package io.pogorzelski.nitro.carriers.service.mapper;

import java.util.ArrayList;
import java.util.List;

import io.pogorzelski.nitro.carriers.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.pogorzelski.nitro.carriers.domain.Address;
import io.pogorzelski.nitro.carriers.domain.CargoType;
import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.domain.Person;
import io.pogorzelski.nitro.carriers.domain.Rating;
import io.pogorzelski.nitro.carriers.service.dto.RatingExtDTO;

@Component
public class RatingExtMapper {

    private final CountryRepository countryRepository;
    private final CarrierRepository carrierRepository;
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final CargoTypeRepository cargoTypeRepository;

    @Autowired
    public RatingExtMapper(CountryRepository countryRepository, CarrierRepository carrierRepository, PersonRepository personRepository, AddressRepository addressRepository, CargoTypeRepository cargoTypeRepository) {
        this.countryRepository = countryRepository;
        this.carrierRepository = carrierRepository;
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
        this.cargoTypeRepository = cargoTypeRepository;
    }

    public List<Rating> toEntity(List<RatingExtDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<Rating> list = new ArrayList<>(dtoList.size());
        for (RatingExtDTO ratingExtDTO : dtoList) {
            list.add(toEntity(ratingExtDTO));
        }

        return list;
    }

    public List<RatingExtDTO> toDto(List<Rating> entityList) {
        if (entityList == null) {
            return null;
        }

        List<RatingExtDTO> list = new ArrayList<>(entityList.size());
        for (Rating rating : entityList) {
            list.add(toDto(rating));
        }

        return list;
    }

    public RatingExtDTO toDto(Rating rating) {
        if (rating == null) {
            return null;
        }

        RatingExtDTO ratingExtDTO = new RatingExtDTO();

        Integer carrierTransId = ratingCarrierTransId(rating);
        if (carrierTransId != null) {
            ratingExtDTO.setCarrierTransId(carrierTransId);
        }
        String carrierName = ratingCarrierName(rating);
        if (carrierName != null) {
            ratingExtDTO.setCarrierName(carrierName);
        }
        Integer personTransId = ratingPersonTransId(rating);
        if (personTransId != null) {
            ratingExtDTO.setPersonTransId(personTransId);
        }
        String firstName = ratingPersonFirstName(rating);
        if (firstName != null) {
            ratingExtDTO.setPersonFirstName(firstName);
        }
        String lastName = ratingPersonLastName(rating);
        if (lastName != null) {
            ratingExtDTO.setPersonLastName(lastName);
        }
        String chargeAddressCountry = ratingChargeAddressCountry(rating);
        if (chargeAddressCountry != null) {
            ratingExtDTO.setChargeAddressCountry(chargeAddressCountry);
        }
        String chargeAddressPostalCode = ratingChargeAddressPostalCode(rating);
        if (chargeAddressPostalCode != null) {
            ratingExtDTO.setChargeAddressPostalCode(chargeAddressPostalCode);
        }
        String dischargeAddressCountry = ratingDischargeAddressCountry(rating);
        if (dischargeAddressCountry != null) {
            ratingExtDTO.setDischargeAddressCountry(dischargeAddressCountry);
        }
        String dischargeAddressPostalCode = ratingDischargeAddressPostalCode(rating);
        if (dischargeAddressPostalCode != null) {
            ratingExtDTO.setDischargeAddressPostalCode(dischargeAddressPostalCode);
        }
        String cargoTypeName = ratingCargoTypeName(rating);
        if (cargoTypeName != null) {
            ratingExtDTO.setCargoTypeName(cargoTypeName);
        }
        Long cargoTypeId = ratingCargoTypeId(rating);
        if (cargoTypeId != null) {
            ratingExtDTO.setCargoTypeId(cargoTypeId);
        }
        ratingExtDTO.setId(rating.getId());
        ratingExtDTO.setFlexibility(rating.getFlexibility());
        ratingExtDTO.setContact(rating.getContact());
        ratingExtDTO.setPrice(rating.getPrice());
        ratingExtDTO.setRecommendation(rating.getRecommendation());
        ratingExtDTO.setAverage(rating.getAverage());

        return ratingExtDTO;
    }

    public Rating toEntity(RatingExtDTO ratingExtDTO) {
        if (ratingExtDTO == null) {
            return null;
        }

        Rating rating = new Rating();


        Integer carrierTransId = ratingExtDTO.getCarrierTransId();
        Carrier carrier = carrierRepository.findByTransId(carrierTransId);
        if (carrier == null) {
            carrier = new Carrier();
            carrier.setName(ratingExtDTO.getCarrierName());
            carrier.setTransId(carrierTransId);
        }

        Integer companyId = ratingExtDTO.getPersonTransId();
        Person person = personRepository.findByCompanyId(companyId);
        if (person == null) {
            person = new Person();
            person.setCarrier(carrier);
            person.setCompanyId(companyId);
            person.setFirstName(ratingExtDTO.getPersonFirstName());
            person.setLastName(ratingExtDTO.getPersonLastName());
        }

        String chargeAddressCountry = ratingExtDTO.getChargeAddressCountry();
        String chargeAddressPostalCode = ratingExtDTO.getChargeAddressPostalCode();
        Country chargeCountry = countryRepository.findByCountryName(chargeAddressCountry);
        if (chargeCountry == null) {
            throw new RuntimeException("Charge country cannot be null!");
        }
        Address chargeAddress = addressRepository.findByCountry_CountryNameAndPostalCode(chargeAddressCountry, chargeAddressPostalCode);
        if (chargeAddress == null) {
            chargeAddress = new Address();
            chargeAddress.setCountry(chargeCountry);
            chargeAddress.setPostalCode(chargeAddressPostalCode);
        }

        String dischargeAddressCountry = ratingExtDTO.getDischargeAddressCountry();
        String dischargeAddressPostalCode = ratingExtDTO.getDischargeAddressPostalCode();
        Country dischargeCountry = countryRepository.findByCountryName(dischargeAddressCountry);
        if (dischargeCountry == null) {
            throw new RuntimeException("Discharge country cannot be null!");
        }
        Address dischargeAddress = addressRepository.findByCountry_CountryNameAndPostalCode(dischargeAddressCountry, dischargeAddressPostalCode);
        if (dischargeAddress == null) {
            dischargeAddress = new Address();
            dischargeAddress.setCountry(dischargeCountry);
            dischargeAddress.setPostalCode(dischargeAddressPostalCode);
        }

        Long cargoTypeId = ratingExtDTO.getCargoTypeId();
        CargoType cargoType = cargoTypeRepository.findById(cargoTypeId)
            .orElseThrow(() -> new RuntimeException("Cargo type cannot be null!"));

        rating.setCarrier(carrier);
        rating.setPerson(person);
        rating.setChargeAddress(chargeAddress);
        rating.setDischargeAddress(dischargeAddress);
        rating.setCargoType(cargoType);

        rating.setId(ratingExtDTO.getId());
        rating.setFlexibility(ratingExtDTO.getFlexibility());
        rating.setContact(ratingExtDTO.getContact());
        rating.setPrice(ratingExtDTO.getPrice());
        rating.setRecommendation(ratingExtDTO.getRecommendation());
        rating.setAverage(ratingExtDTO.getAverage());

        return rating;
    }

    private String ratingCargoTypeName(Rating rating) {
        if (rating == null) {
            return null;
        }
        CargoType cargoType = rating.getCargoType();
        if (cargoType == null) {
            return null;
        }
        return cargoType.getName();
    }

    private String ratingCarrierName(Rating rating) {
        if (rating == null) {
            return null;
        }
        Carrier carrier = rating.getCarrier();
        if (carrier == null) {
            return null;
        }
        return carrier.getName();
    }

    private String ratingDischargeAddressPostalCode(Rating rating) {
        if (rating == null) {
            return null;
        }
        Address dischargeAddress = rating.getDischargeAddress();
        if (dischargeAddress == null) {
            return null;
        }
        return dischargeAddress.getPostalCode();
    }

    private String ratingDischargeAddressCountry(Rating rating) {
        if (rating == null) {
            return null;
        }
        Address dischargeAddress = rating.getDischargeAddress();
        if (dischargeAddress == null) {
            return null;
        }
        Country country = dischargeAddress.getCountry();
        if (country == null) {
            return null;
        }
        return country.getCountryName();
    }

    private Integer ratingPersonTransId(Rating rating) {
        if (rating == null) {
            return null;
        }
        Person person = rating.getPerson();
        if (person == null) {
            return null;
        }
        return person.getCompanyId();
    }

    private String ratingPersonFirstName(Rating rating) {
        if (rating == null) {
            return null;
        }
        Person person = rating.getPerson();
        if (person == null) {
            return null;
        }
        return person.getFirstName();
    }

    private String ratingPersonLastName(Rating rating) {
        if (rating == null) {
            return null;
        }
        Person person = rating.getPerson();
        if (person == null) {
            return null;
        }
        return person.getLastName();
    }

    private Integer ratingCarrierTransId(Rating rating) {
        if (rating == null) {
            return null;
        }
        Carrier carrier = rating.getCarrier();
        if (carrier == null) {
            return null;
        }
        return carrier.getTransId();
    }

    private String ratingChargeAddressCountry(Rating rating) {
        if (rating == null) {
            return null;
        }
        Address chargeAddress = rating.getChargeAddress();
        if (chargeAddress == null) {
            return null;
        }
        Country country = chargeAddress.getCountry();
        if (country == null) {
            return null;
        }
        return country.getCountryName();
    }

    private String ratingChargeAddressPostalCode(Rating rating) {
        if (rating == null) {
            return null;
        }
        Address chargeAddress = rating.getChargeAddress();
        if (chargeAddress == null) {
            return null;
        }
        return chargeAddress.getPostalCode();
    }

    private Long ratingCargoTypeId(Rating rating) {
        if (rating == null) {
            return null;
        }
        CargoType cargoType = rating.getCargoType();
        if (cargoType == null) {
            return null;
        }
        return cargoType.getId();
    }
}

