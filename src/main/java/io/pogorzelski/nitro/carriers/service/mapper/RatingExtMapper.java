package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.repository.*;
import io.pogorzelski.nitro.carriers.service.dto.RatingExtDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RatingExtMapper implements EntityMapper<RatingExtDTO, Rating>{

    private final CountryRepository countryRepository;
    private final CarrierRepository carrierRepository;
    private final PersonRepository personRepository;
    private final CargoTypeRepository cargoTypeRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public RatingExtMapper(CountryRepository countryRepository, CarrierRepository carrierRepository, PersonRepository personRepository, CargoTypeRepository cargoTypeRepository, RatingRepository ratingRepository) {
        this.countryRepository = countryRepository;
        this.carrierRepository = carrierRepository;
        this.personRepository = personRepository;
        this.cargoTypeRepository = cargoTypeRepository;
        this.ratingRepository = ratingRepository;
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
        ratingExtDTO.setDistance(rating.getDistance());
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


        String dischargeAddressCountry = ratingExtDTO.getDischargeAddressCountry();
        String dischargeAddressPostalCode = ratingExtDTO.getDischargeAddressPostalCode();
        Country dischargeCountry = countryRepository.findByCountryName(dischargeAddressCountry);
        if (dischargeCountry == null) {
            throw new RuntimeException("Discharge country cannot be null!");
        }

        Long cargoTypeId = ratingExtDTO.getCargoTypeId();
        CargoType cargoType = cargoTypeRepository.findById(cargoTypeId)
            .orElseThrow(() -> new RuntimeException("Cargo type cannot be null!"));

        rating.setCarrier(carrier);
        rating.setPerson(person);
        rating.setChargeCountry(chargeCountry);
        rating.setChargePostalCode(chargeAddressPostalCode);
        rating.setDischargeCountry(dischargeCountry);
        rating.setDischargePostalCode(dischargeAddressPostalCode);
        rating.setCargoType(cargoType);

        rating.setId(ratingExtDTO.getId());
        rating.setDistance(ratingExtDTO.getDistance());
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
        return rating.getDischargePostalCode();
    }

    private String ratingDischargeAddressCountry(Rating rating) {
        if (rating == null) {
            return null;
        }
        Country country = rating.getDischargeCountry();
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

        Country country = rating.getChargeCountry();
        if (country == null) {
            return null;
        }
        return country.getCountryName();
    }

    private String ratingChargeAddressPostalCode(Rating rating) {
        if (rating == null) {
            return null;
        }
        return rating.getChargePostalCode();
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

    @Override
    public Rating fromId(Long id) {
        if (id == null) {
            return null;
        }
        return ratingRepository.findById(id).orElse(new Rating(){{setId(id);}});
    }
}

