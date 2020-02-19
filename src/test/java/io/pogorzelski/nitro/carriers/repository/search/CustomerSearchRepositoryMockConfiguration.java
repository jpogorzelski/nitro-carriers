package io.pogorzelski.nitro.carriers.repository.search;

import io.pogorzelski.nitro.carriers.service.CustomerService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CustomerSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CustomerSearchRepositoryMockConfiguration {

    @MockBean
    private CustomerSearchRepository mockCustomerSearchRepository;


    @SpyBean
    private CustomerService mockCustomerService;
}
