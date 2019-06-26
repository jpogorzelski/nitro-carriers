package io.pogorzelski.nitro.carriers.repository;

import io.pogorzelski.nitro.carriers.domain.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data  repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("select rating from Rating rating where rating.createdBy.login = ?#{principal.username}")
    List<Rating> findByCreatedByIsCurrentUser();

    Page<Rating> findByCarrier_Id(Pageable pageable, Long carrierId);

}
