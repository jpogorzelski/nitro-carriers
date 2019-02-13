package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.service.dto.RatingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RatingMapper implements EntityMapper<RatingDTO, Rating> {

    private final CarrierMapper carrierMapper;
    private final PersonMapper personMapper;
    private final CountryMapper countryMapper;
    private final CargoTypeMapper cargoTypeMapper;

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingMapper(CarrierMapper carrierMapper, PersonMapper personMapper, CountryMapper countryMapper, CargoTypeMapper cargoTypeMapper, RatingRepository ratingRepository) {
        this.carrierMapper = carrierMapper;
        this.personMapper = personMapper;
        this.countryMapper = countryMapper;
        this.cargoTypeMapper = cargoTypeMapper;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<Rating> toEntity(List<RatingDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<Rating> list = new ArrayList<>(dtoList.size());
        for (RatingDTO ratingDTO : dtoList) {
            list.add(toEntity(ratingDTO));
        }

        return list;
    }

    @Override
    public List<RatingDTO> toDto(List<Rating> entityList) {
        if (entityList == null) {
            return null;
        }

        List<RatingDTO> list = new ArrayList<>(entityList.size());
        for (Rating rating : entityList) {
            list.add(toDto(rating));
        }

        return list;
    }

    @Override
    public RatingDTO toDto(Rating rating) {
        if (rating == null) {
            return null;
        }

        RatingDTO ratingDTO = new RatingDTO();

        String name = ratingCargoTypeName(rating);
        if (name != null) {
            ratingDTO.setCargoTypeName(name);
        }
        String name1 = ratingCarrierName(rating);
        if (name1 != null) {
            ratingDTO.setCarrierName(name1);
        }
        Country chargeCountry = ratingChargeAddressCountry(rating);
        if (chargeCountry != null) {
            ratingDTO.setChargeCountryId(chargeCountry.getId());
            ratingDTO.setChargeCountryCountryName(chargeCountry.getCountryName());
        }

        String chargeAddressPostalCode = ratingChargeAddressPostalCode(rating);
        if (chargeAddressPostalCode != null) {
            ratingDTO.setChargePostalCode(chargeAddressPostalCode);
        }
        Country dischargeCountry = ratingDischargeAddressCountry(rating);
        if (dischargeCountry != null) {
            ratingDTO.setDischargeCountryId(dischargeCountry.getId());
            ratingDTO.setDischargeCountryCountryName(dischargeCountry.getCountryName());
        }

        String dischargeAddressPostalCode = ratingDischargeAddressPostalCode(rating);
        if (dischargeAddressPostalCode != null) {
            ratingDTO.setDischargePostalCode(dischargeAddressPostalCode);
        }

        Long id1 = ratingPersonId(rating);
        if (id1 != null) {
            ratingDTO.setPersonId(id1);
        }
        Long id2 = ratingCarrierId(rating);
        if (id2 != null) {
            ratingDTO.setCarrierId(id2);
        }
        String firstName = ratingPersonFirstName(rating);
        if (firstName != null) {
            ratingDTO.setPersonFirstName(firstName);
        }

        Long id4 = ratingCargoTypeId(rating);
        if (id4 != null) {
            ratingDTO.setCargoTypeId(id4);
        }
        ratingDTO.setId(rating.getId());
        ratingDTO.setContact(rating.getContact());
        ratingDTO.setPrice(rating.getPrice());
        ratingDTO.setFlexibility(rating.getFlexibility());
        ratingDTO.setRecommendation(rating.getRecommendation());
        ratingDTO.setAverage(rating.getAverage());

        return ratingDTO;
    }

    @Override
    public Rating fromId(Long id) {
        if (id == null) {
            return null;
        }
        return ratingRepository.findById(id).orElse(new Rating(){{setId(id);}});
    }

    @Override
    public Rating toEntity(RatingDTO ratingDTO) {
        if (ratingDTO == null) {
            return null;
        }

        Rating rating = new Rating();

        rating.setCarrier(carrierMapper.fromId(ratingDTO.getCarrierId()));
        rating.setPerson(personMapper.fromId(ratingDTO.getPersonId()));
        rating.setChargeCountry(countryMapper.fromId(ratingDTO.getChargeCountryId()));
        rating.setDischargeCountry(countryMapper.fromId(ratingDTO.getDischargeCountryId()));
        rating.setCargoType(cargoTypeMapper.fromId(ratingDTO.getCargoTypeId()));
        rating.setChargePostalCode(ratingDTO.getChargePostalCode());
        rating.setDischargePostalCode(ratingDTO.getDischargePostalCode());
        rating.setId(ratingDTO.getId());
        rating.setContact(ratingDTO.getContact());
        rating.setPrice(ratingDTO.getPrice());
        rating.setFlexibility(ratingDTO.getFlexibility());
        rating.setRecommendation(ratingDTO.getRecommendation());
        rating.setAverage(ratingDTO.getAverage());

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

    private Country ratingChargeAddressCountry(Rating rating) {
        if (rating == null) {
            return null;
        }

        return rating.getChargeCountry();
    }

    private Country ratingDischargeAddressCountry(Rating rating) {
        if (rating == null) {
            return null;
        }

        return rating.getDischargeCountry();
    }

    private String ratingDischargeAddressPostalCode(Rating rating) {
        if (rating == null) {
            return null;
        }
        return rating.getDischargePostalCode();
    }

    private Long ratingPersonId(Rating rating) {
        if (rating == null) {
            return null;
        }
        Person person = rating.getPerson();
        if (person == null) {
            return null;
        }
        return person.getId();
    }

    private Long ratingCarrierId(Rating rating) {
        if (rating == null) {
            return null;
        }
        Carrier carrier = rating.getCarrier();
        if (carrier == null) {
            return null;
        }
        return carrier.getId();
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
}
