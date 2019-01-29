package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.CargoType;
import io.pogorzelski.nitro.carriers.repository.CargoTypeRepository;
import io.pogorzelski.nitro.carriers.service.dto.CargoTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CargoTypeMapper implements EntityMapper<CargoTypeDTO, CargoType> {

    private final CargoTypeRepository cargoTypeRepository;

    @Autowired
    public CargoTypeMapper(CargoTypeRepository cargoTypeRepository) {
        this.cargoTypeRepository = cargoTypeRepository;
    }

    @Override
    public CargoType fromId(Long id) {
        if (id == null) {
            return null;
        }
        return cargoTypeRepository.findById(id).orElse(new CargoType(){{setId(id);}});
    }

    @Override
    public CargoType toEntity(CargoTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        CargoType cargoType = new CargoType();

        cargoType.setId(dto.getId());
        cargoType.setName(dto.getName());

        return cargoType;
    }

    @Override
    public CargoTypeDTO toDto(CargoType entity) {
        if (entity == null) {
            return null;
        }

        CargoTypeDTO cargoTypeDTO = new CargoTypeDTO();

        cargoTypeDTO.setId(entity.getId());
        cargoTypeDTO.setName(entity.getName());

        return cargoTypeDTO;
    }

    @Override
    public List<CargoType> toEntity(List<CargoTypeDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<CargoType> list = new ArrayList<>(dtoList.size());
        for (CargoTypeDTO cargoTypeDTO : dtoList) {
            list.add(toEntity(cargoTypeDTO));
        }

        return list;
    }

    @Override
    public List<CargoTypeDTO> toDto(List<CargoType> entityList) {
        if (entityList == null) {
            return null;
        }

        List<CargoTypeDTO> list = new ArrayList<>(entityList.size());
        for (CargoType cargoType : entityList) {
            list.add(toDto(cargoType));
        }

        return list;
    }
}
