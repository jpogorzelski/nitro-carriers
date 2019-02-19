package io.pogorzelski.nitro.carriers.repository.search;

import io.pogorzelski.nitro.carriers.service.RatingExtService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RatingSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RatingSearchRepositoryMockConfiguration {

    @MockBean
    private RatingSearchRepository mockRatingSearchRepository;

    @SpyBean
    private RatingExtService mockRatingExtService;

}
