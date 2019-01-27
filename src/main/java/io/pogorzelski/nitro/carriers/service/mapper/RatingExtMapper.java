package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.domain.Rating;
import io.pogorzelski.nitro.carriers.service.dto.RatingExtDTO;

import io.pogorzelski.nitro.carriers.domain.Address;
import io.pogorzelski.nitro.carriers.domain.CargoType;
import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.domain.Person;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RatingExtMapper {

    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private CargoTypeMapper cargoTypeMapper;
    @Autowired
    private CarrierMapper carrierMapper;

    public List<Rating> toEntity(List<RatingExtDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<Rating> list = new ArrayList<Rating>(dtoList.size());
        for (RatingExtDTO ratingExtDTO : dtoList) {
            list.add(toEntity(ratingExtDTO));
        }

        return list;
    }

    public List<RatingExtDTO> toDto(List<Rating> entityList) {
        if (entityList == null) {
            return null;
        }

        List<RatingExtDTO> list = new ArrayList<RatingExtDTO>(entityList.size());
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

        String cargoTypeName = ratingCargoTypeName(rating);
        if (cargoTypeName != null) {
            ratingExtDTO.setCargoTypeName(cargoTypeName);
        }
        String carrierName = ratingCarrierName(rating);
        if (carrierName != null) {
            ratingExtDTO.setCarrierName(carrierName);
        }
        String dischargeAddressPostalCode = ratingDischargeAddressPostalCode(rating);
        if (dischargeAddressPostalCode != null) {
            ratingExtDTO.setDischargeAddressPostalCode(dischargeAddressPostalCode);
        }

        String dischargeAddressCountry = ratingDischargeAddressCountry(rating);
        if (dischargeAddressCountry != null) {
            ratingExtDTO.setDischargeAddressCountry(dischargeAddressCountry);
        }
        Integer personTransId = ratingPersonTransId(rating);
        if (personTransId != null) {
            ratingExtDTO.setPersonTransId(personTransId);
        }
        String firstName = ratingPersonFirstName(rating);
        if (firstName != null) {
            ratingExtDTO.setPersonFirstName(firstName);
        }
        Integer carrierTransId = ratingCarrierTransId(rating);
        if (carrierTransId != null) {
            ratingExtDTO.setCarrierTransId(carrierTransId);
        }
        String chargeAddressCountry = ratingChargeAddressCountry(rating);
        if (chargeAddressCountry != null) {
            ratingExtDTO.setChargeAddressCountry(chargeAddressCountry);
        }
        String postalCode1 = ratingChargeAddressPostalCode(rating);
        if (postalCode1 != null) {
            ratingExtDTO.setChargeAddressPostalCode(postalCode1);
        }
        Long id4 = ratingCargoTypeId(rating);
        if (id4 != null) {
            ratingExtDTO.setCargoTypeId(id4);
        }
        ratingExtDTO.setId(rating.getId());
        ratingExtDTO.setFlexibiliy(rating.getFlexibiliy());
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

       /* final CargoType cargoType = cargoTypeMapper.fromId(ratingExtDTO.getCargoTypeId());
        final Address chargeAddress = addressMapper.fromId(ratingExtDTO.getChargeAddressId());
        final Carrier carrier = carrierMapper.fromId(ratingExtDTO.getCarrierId());
        final Person person = personMapper.fromId(ratingExtDTO.getPersonId());
        final Address dischargeAddress = addressMapper.fromId(ratingExtDTO.getDischargeAddressId());

        rating.setCarrier(carrier);
        rating.setPerson(person);
        rating.setChargeAddress(chargeAddress);
        rating.setDischargeAddress(dischargeAddress);
        rating.setCargoType(cargoType);*/
        rating.setId(ratingExtDTO.getId());
        rating.setFlexibiliy(ratingExtDTO.getFlexibiliy());
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
        String name = cargoType.getName();
        if (name == null) {
            return null;
        }
        return name;
    }

    private String ratingCarrierName(Rating rating) {
        if (rating == null) {
            return null;
        }
        Carrier carrier = rating.getCarrier();
        if (carrier == null) {
            return null;
        }
        String name = carrier.getName();
        if (name == null) {
            return null;
        }
        return name;
    }

    private String ratingDischargeAddressPostalCode(Rating rating) {
        if (rating == null) {
            return null;
        }
        Address dischargeAddress = rating.getDischargeAddress();
        if (dischargeAddress == null) {
            return null;
        }
        String postalCode = dischargeAddress.getPostalCode();
        if (postalCode == null) {
            return null;
        }
        return postalCode;
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
        String id = person.getCompanyId();
        if (id == null) {
            return null;
        }
        return Integer.valueOf(id);
    }

    private String ratingPersonFirstName(Rating rating) {
        if (rating == null) {
            return null;
        }
        Person person = rating.getPerson();
        if (person == null) {
            return null;
        }
        String firstName = person.getFirstName();
        if (firstName == null) {
            return null;
        }
        return firstName;
    }
    private String ratingPersonLastName(Rating rating) {
        if (rating == null) {
            return null;
        }
        Person person = rating.getPerson();
        if (person == null) {
            return null;
        }
        String firstName = person.getLastName();
        if (firstName == null) {
            return null;
        }
        return firstName;
    }

    private Integer ratingCarrierTransId(Rating rating) {
        if (rating == null) {
            return null;
        }
        Carrier carrier = rating.getCarrier();
        if (carrier == null) {
            return null;
        }
        Integer id = carrier.getTransId();
        if (id == null) {
            return null;
        }
        return id;
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
        String postalCode = chargeAddress.getPostalCode();
        if (postalCode == null) {
            return null;
        }
        return postalCode;
    }

    private Long ratingCargoTypeId(Rating rating) {
        if (rating == null) {
            return null;
        }
        CargoType cargoType = rating.getCargoType();
        if (cargoType == null) {
            return null;
        }
        Long id = cargoType.getId();
        if (id == null) {
            return null;
        }
        return id;
    }
}

