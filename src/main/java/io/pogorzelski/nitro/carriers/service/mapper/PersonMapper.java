package io.pogorzelski.nitro.carriers.service.mapper;

import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.domain.Person;
import io.pogorzelski.nitro.carriers.repository.PersonRepository;
import io.pogorzelski.nitro.carriers.service.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonMapper implements EntityMapper<PersonDTO, Person> {

    private final CarrierMapper carrierMapper;
    private final PersonRepository personRepository;

    @Autowired
    public PersonMapper(CarrierMapper carrierMapper, PersonRepository personRepository) {
        this.carrierMapper = carrierMapper;
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> toEntity(List<PersonDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<Person> list = new ArrayList<>(dtoList.size());
        for (PersonDTO personDTO : dtoList) {
            list.add(toEntity(personDTO));
        }

        return list;
    }

    @Override
    public List<PersonDTO> toDto(List<Person> entityList) {
        if (entityList == null) {
            return null;
        }

        List<PersonDTO> list = new ArrayList<>(entityList.size());
        for (Person person : entityList) {
            list.add(toDto(person));
        }

        return list;
    }

    @Override
    public PersonDTO toDto(Person person) {
        if (person == null) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();

        String name = personCarrierName(person);
        if (name != null) {
            personDTO.setCarrierName(name);
        }
        Long id = personCarrierId(person);
        if (id != null) {
            personDTO.setCarrierId(id);
        }
        personDTO.setId(person.getId());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setCompanyId(person.getCompanyId());
        personDTO.setPhoneNumber(person.getPhoneNumber());

        return personDTO;
    }

    @Override
    public Person fromId(Long id) {
        if (id == null) {
            return null;
        }
        return personRepository.findById(id).orElse(new Person(){{setId(id);}});
    }

    @Override
    public Person toEntity(PersonDTO personDTO) {
        if (personDTO == null) {
            return null;
        }

        Person person = new Person();

        person.setCarrier(carrierMapper.fromId(personDTO.getCarrierId()));
        person.setId(personDTO.getId());
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setCompanyId(personDTO.getCompanyId());
        person.setPhoneNumber(personDTO.getPhoneNumber());

        return person;
    }

    private String personCarrierName(Person person) {
        if (person == null) {
            return null;
        }
        Carrier carrier = person.getCarrier();
        if (carrier == null) {
            return null;
        }
        return carrier.getName();
    }

    private Long personCarrierId(Person person) {
        if (person == null) {
            return null;
        }
        Carrier carrier = person.getCarrier();
        if (carrier == null) {
            return null;
        }
        return carrier.getId();
    }
}
