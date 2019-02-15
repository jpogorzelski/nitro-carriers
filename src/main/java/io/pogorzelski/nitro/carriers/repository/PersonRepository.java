package io.pogorzelski.nitro.carriers.repository;

import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.domain.Person;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByCompanyId(Integer companyId);

    Person findByCarrier_TransIdAndCompanyId(Integer carrierTransId, Integer companyId);
}
