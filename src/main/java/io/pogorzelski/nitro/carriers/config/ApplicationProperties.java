package io.pogorzelski.nitro.carriers.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Nitro Carriers.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

}
