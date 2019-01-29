package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.repository.CountryRepository;
import io.pogorzelski.nitro.carriers.service.dto.CountryDTO;
import io.pogorzelski.nitro.carriers.service.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CountryMapper implements EntityMapper<CountryDTO, Country> {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryMapper(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country fromId(Long id) {
        if (id == null) {
            return null;
        }
        return countryRepository.findById(id).orElse(new Country(){{setId(id);}});
    }

    @Override
    public Country toEntity(CountryDTO dto) {
        if (dto == null) {
            return null;
        }

        Country country = new Country();

        country.setId(dto.getId());
        country.setCountryName(dto.getCountryName());

        return country;
    }

    @Override
    public CountryDTO toDto(Country entity) {
        if (entity == null) {
            return null;
        }

        CountryDTO countryDTO = new CountryDTO();

        countryDTO.setId(entity.getId());
        countryDTO.setCountryName(entity.getCountryName());

        return countryDTO;
    }

    @Override
    public List<Country> toEntity(List<CountryDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<Country> list = new ArrayList<>(dtoList.size());
        for (CountryDTO countryDTO : dtoList) {
            list.add(toEntity(countryDTO));
        }

        return list;
    }

    @Override
    public List<CountryDTO> toDto(List<Country> entityList) {
        if (entityList == null) {
            return null;
        }

        List<CountryDTO> list = new ArrayList<>(entityList.size());
        for (Country country : entityList) {
            list.add(toDto(country));
        }

        return list;
    }
}
