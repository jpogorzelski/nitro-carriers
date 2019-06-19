package io.pogorzelski.nitro.carriers.repository.search;

import io.pogorzelski.nitro.carriers.domain.City;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the City entity.
 */
public interface CitySearchRepository extends ElasticsearchRepository<City, Long> {
}
