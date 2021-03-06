package io.pogorzelski.nitro.carriers.repository.search;

import io.pogorzelski.nitro.carriers.domain.Rating;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Rating entity.
 */
public interface RatingSearchRepository extends ElasticsearchRepository<Rating, Long> {
}
