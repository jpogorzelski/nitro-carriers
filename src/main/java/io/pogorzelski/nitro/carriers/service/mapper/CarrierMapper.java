package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.service.dto.CarrierDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Carrier and its DTO CarrierDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CarrierMapper extends EntityMapper<CarrierDTO, Carrier> {


    @Mapping(target = "people", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    Carrier toEntity(CarrierDTO carrierDTO);

    default Carrier fromId(Long id) {
        if (id == null) {
            return null;
        }
        Carrier carrier = new Carrier();
        carrier.setId(id);
        return carrier;
    }
}
