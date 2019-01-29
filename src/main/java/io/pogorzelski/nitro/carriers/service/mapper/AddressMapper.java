package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.Address;
import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.repository.AddressRepository;
import io.pogorzelski.nitro.carriers.service.dto.AddressDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressMapper implements EntityMapper<AddressDTO, Address> {

    private final CountryMapper countryMapper;
    private final AddressRepository addressRepository;

    @Autowired
    public AddressMapper(CountryMapper countryMapper, AddressRepository addressRepository) {
        this.countryMapper = countryMapper;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> toEntity(List<AddressDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<Address> list = new ArrayList<>(dtoList.size());
        for (AddressDTO addressDTO : dtoList) {
            list.add(toEntity(addressDTO));
        }

        return list;
    }

    @Override
    public List<AddressDTO> toDto(List<Address> entityList) {
        if (entityList == null) {
            return null;
        }

        List<AddressDTO> list = new ArrayList<>(entityList.size());
        for (Address address : entityList) {
            list.add(toDto(address));
        }

        return list;
    }

    @Override
    public AddressDTO toDto(Address address) {
        if (address == null) {
            return null;
        }

        AddressDTO addressDTO = new AddressDTO();

        String countryName = addressCountryCountryName(address);
        if (countryName != null) {
            addressDTO.setCountryCountryName(countryName);
        }
        Long id = addressCountryId(address);
        if (id != null) {
            addressDTO.setCountryId(id);
        }
        addressDTO.setId(address.getId());
        addressDTO.setPostalCode(address.getPostalCode());

        return addressDTO;
    }

    @Override
    public Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        return addressRepository.findById(id).orElse(new Address(){{setId(id);}});
    }

    @Override
    public Address toEntity(AddressDTO addressDTO) {
        if (addressDTO == null) {
            return null;
        }

        Address address = new Address();

        address.setCountry(countryMapper.fromId(addressDTO.getCountryId()));
        address.setId(addressDTO.getId());
        address.setPostalCode(addressDTO.getPostalCode());

        return address;
    }

    private String addressCountryCountryName(Address address) {
        if (address == null) {
            return null;
        }
        Country country = address.getCountry();
        if (country == null) {
            return null;
        }
        return country.getCountryName();
    }

    private Long addressCountryId(Address address) {
        if (address == null) {
            return null;
        }
        Country country = address.getCountry();
        if (country == null) {
            return null;
        }
        return country.getId();
    }
}
