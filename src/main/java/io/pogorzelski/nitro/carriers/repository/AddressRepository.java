package io.pogorzelski.nitro.carriers.repository;

import io.pogorzelski.nitro.carriers.domain.Address;
import io.pogorzelski.nitro.carriers.domain.Country;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByCountry_CountryNameAndPostalCode(String countryName, String postalCode);

    Address findByCountryAndPostalCode(Country country, String postalCode);
}
