package io.pogorzelski.nitro.carriers.repository;

import io.pogorzelski.nitro.carriers.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select customer from Customer customer where customer.user.login = ?#{principal.username} or customer.state = io.pogorzelski.nitro.carriers.domain.enumeration.CustomerState.AVAILABLE")
    Page<Customer> findByUserIsCurrentUserOrAvailable(Pageable pageable);

    @Query("select customer from Customer customer " +
        "where customer.id = :id and customer.user.login = ?#{principal.username}")
    Optional<Customer> findByIdAndUserIsCurrentUser(@Param("id") Long id);

}
