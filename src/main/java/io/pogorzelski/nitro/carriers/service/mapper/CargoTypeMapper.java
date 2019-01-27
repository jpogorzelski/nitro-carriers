package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.service.dto.CargoTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CargoType and its DTO CargoTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CargoTypeMapper extends EntityMapper<CargoTypeDTO, CargoType> {



    default CargoType fromId(Long id) {
        if (id == null) {
            return null;
        }
        CargoType cargoType = new CargoType();
        cargoType.setId(id);
        return cargoType;
    }
}
