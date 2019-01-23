package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.service.dto.RatingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Rating and its DTO RatingDTO.
 */
@Mapper(componentModel = "spring", uses = {PersonMapper.class, AddressMapper.class, CargoTypeMapper.class, CarrierMapper.class})
public interface RatingMapper extends EntityMapper<RatingDTO, Rating> {

    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "chargeAddress.id", target = "chargeAddressId")
    @Mapping(source = "dischargeAddress.id", target = "dischargeAddressId")
    @Mapping(source = "cargoType.id", target = "cargoTypeId")
    @Mapping(source = "carrier.id", target = "carrierId")
    RatingDTO toDto(Rating rating);

    @Mapping(source = "personId", target = "person")
    @Mapping(source = "chargeAddressId", target = "chargeAddress")
    @Mapping(source = "dischargeAddressId", target = "dischargeAddress")
    @Mapping(source = "cargoTypeId", target = "cargoType")
    @Mapping(source = "carrierId", target = "carrier")
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
