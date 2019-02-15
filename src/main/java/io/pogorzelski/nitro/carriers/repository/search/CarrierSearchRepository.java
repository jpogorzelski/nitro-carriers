package io.pogorzelski.nitro.carriers.repository.search;

import io.pogorzelski.nitro.carriers.domain.Carrier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Carrier entity.
 */
public interface CarrierSearchRepository extends ElasticsearchRepository<Carrier, Long> {
}
