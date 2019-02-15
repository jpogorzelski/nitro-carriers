package io.pogorzelski.nitro.carriers.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CargoTypeSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CargoTypeSearchRepositoryMockConfiguration {

    @MockBean
    private CargoTypeSearchRepository mockCargoTypeSearchRepository;

}
