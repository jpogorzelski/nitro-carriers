package io.pogorzelski.nitro.carriers.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CarrierSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CarrierSearchRepositoryMockConfiguration {

    @MockBean
    private CarrierSearchRepository mockCarrierSearchRepository;

}
