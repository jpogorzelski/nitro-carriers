package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.repository.CarrierRepository;
import io.pogorzelski.nitro.carriers.service.dto.CarrierDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarrierMapper implements EntityMapper<CarrierDTO, Carrier> {

    private final CarrierRepository carrierRepository;

    @Autowired
    public CarrierMapper(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    @Override
    public CarrierDTO toDto(Carrier entity) {
        if (entity == null) {
            return null;
        }

        CarrierDTO carrierDTO = new CarrierDTO();

        carrierDTO.setId(entity.getId());
        carrierDTO.setName(entity.getName());
        carrierDTO.setTransId(entity.getTransId());

        return carrierDTO;
    }

    @Override
    public List<Carrier> toEntity(List<CarrierDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<Carrier> list = new ArrayList<>(dtoList.size());
        for (CarrierDTO carrierDTO : dtoList) {
            list.add(toEntity(carrierDTO));
        }

        return list;
    }

    @Override
    public List<CarrierDTO> toDto(List<Carrier> entityList) {
        if (entityList == null) {
            return null;
        }

        List<CarrierDTO> list = new ArrayList<>(entityList.size());
        for (Carrier carrier : entityList) {
            list.add(toDto(carrier));
        }

        return list;
    }

    @Override
    public Carrier fromId(Long id) {
        if (id == null) {
            return null;
        }
        return carrierRepository.findById(id).orElse(new Carrier(){{setId(id);}});
    }

    @Override
    public Carrier toEntity(CarrierDTO carrierDTO) {
        if (carrierDTO == null) {
            return null;
        }

        Carrier carrier = new Carrier();

        carrier.setId(carrierDTO.getId());
        carrier.setName(carrierDTO.getName());
        carrier.setTransId(carrierDTO.getTransId());

        return carrier;
    }
}
