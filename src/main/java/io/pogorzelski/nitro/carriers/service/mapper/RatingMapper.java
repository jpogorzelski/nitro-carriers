package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.service.dto.RatingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Rating and its DTO RatingDTO.
 */
@Mapper(componentModel = "spring", uses = {CarrierMapper.class, PersonMapper.class, AddressMapper.class, CargoTypeMapper.class})
public interface RatingMapper extends EntityMapper<RatingDTO, Rating> {

    @Mapping(source = "carrier.id", target = "carrierId")
    @Mapping(source = "carrier.name", target = "carrierName")
    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "person.firstName", target = "personFirstName")
    @Mapping(source = "chargeAddress.id", target = "chargeAddressId")
    @Mapping(source = "chargeAddress.postalCode", target = "chargeAddressPostalCode")
    @Mapping(source = "dischargeAddress.id", target = "dischargeAddressId")
    @Mapping(source = "dischargeAddress.postalCode", target = "dischargeAddressPostalCode")
    @Mapping(source = "cargoType.id", target = "cargoTypeId")
    @Mapping(source = "cargoType.name", target = "cargoTypeName")
    RatingDTO toDto(Rating rating);

    @Mapping(source = "carrierId", target = "carrier")
    @Mapping(source = "personId", target = "person")
    @Mapping(source = "chargeAddressId", target = "chargeAddress")
    @Mapping(source = "dischargeAddressId", target = "dischargeAddress")
    @Mapping(source = "cargoTypeId", target = "cargoType")
    Rating toEntity(RatingDTO ratingDTO);

    default Rating fromId(Long id) {
        if (id == null) {
            return null;
        }
        Rating rating = new Rating();
        rating.setId(id);
        return rating;
    }
}
