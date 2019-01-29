package io.pogorzelski.nitro.carriers.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(io.pogorzelski.nitro.carriers.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Country.class.getName(), jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Address.class.getName(), jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Carrier.class.getName(), jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Carrier.class.getName() + ".people", jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Carrier.class.getName() + ".ratings", jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Person.class.getName(), jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.CargoType.class.getName(), jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Rating.class.getName(), jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Address.class.getName() + ".countries", jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Rating.class.getName() + ".people", jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Rating.class.getName() + ".chargeAddresses", jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Rating.class.getName() + ".dischargeAddresses", jcacheConfiguration);
            cm.createCache(io.pogorzelski.nitro.carriers.domain.Rating.class.getName() + ".cargoTypes", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
