package io.pogorzelski.nitro.carriers.repository.search;

import io.pogorzelski.nitro.carriers.domain.CargoType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CargoType entity.
 */
public interface CargoTypeSearchRepository extends ElasticsearchRepository<CargoType, Long> {
}
