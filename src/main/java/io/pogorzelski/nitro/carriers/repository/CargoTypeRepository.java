package io.pogorzelski.nitro.carriers.repository;

import io.pogorzelski.nitro.carriers.domain.CargoType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CargoType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CargoTypeRepository extends JpaRepository<CargoType, Long> {

}
