package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.City;
import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.domain.Customer;
import io.pogorzelski.nitro.carriers.domain.User;
import io.pogorzelski.nitro.carriers.domain.enumeration.CustomerState;
import io.pogorzelski.nitro.carriers.repository.CityRepository;
import io.pogorzelski.nitro.carriers.repository.CountryRepository;
import io.pogorzelski.nitro.carriers.repository.CustomerRepository;
import io.pogorzelski.nitro.carriers.repository.search.CustomerSearchRepository;
import io.pogorzelski.nitro.carriers.security.AuthoritiesConstants;
import io.pogorzelski.nitro.carriers.security.SecurityUtils;
import io.pogorzelski.nitro.carriers.service.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Customer.
 */
@Service
@Transactional
public class CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final CityService cityService;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    private final CustomerSearchRepository customerSearchRepository;

    public CustomerService(CustomerRepository customerRepository, UserService userService, CityService cityService, CountryRepository countryService, CityRepository cityRepository, CustomerSearchRepository customerSearchRepository) {
        this.customerRepository = customerRepository;
        this.userService = userService;
        this.cityService = cityService;
        this.countryRepository = countryService;
        this.cityRepository = cityRepository;
        this.customerSearchRepository = customerSearchRepository;
    }

    /**
     * Save a customer.
     *
     * @param customer the entity to save
     * @return the persisted entity
     */
    public Customer save(Customer customer) {
        log.debug("Request to save Customer : {}", customer);
        final User loggedInUser = getUser();
        if (customer.getId() == null) {
            customer.setUser(loggedInUser);
        } else {
            if (!isAdminOrAssigned(customer)) {
                //reject if not assigned or not ADMIN
                log.error("User not allowed to save customer={}", customer);
                return null;
            }
        }

        final Optional<Country> country = Optional.ofNullable(customer.getCountry())
            .map(Country::getCountryNamePL)
            .map(countryRepository::findByCountryNamePL);
        country.ifPresent(customer::setCountry);

        if (customer.getCity() != null){
            String addressCity = customer.getCity().getCityName();
            City city = cityRepository.findByCityName(addressCity);
            if (city == null) {
                City cityDTO = customer.getCity();
                country.ifPresent(cityDTO::setCountry);
                city = cityService.save(cityDTO);
                cityRepository.flush();
            }
            customer.setCity(city);
        }

        Customer result = customerRepository.save(customer);
        customerSearchRepository.save(result);
        return result;
    }

    public User getUser() {
        return userService.getUserWithAuthorities()
            .orElseThrow(() -> new AccessDeniedException("You are not logged in!"));
    }

    /**
     * Get all the customers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Customers");
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            return customerRepository.findAll(pageable).map(CustomerDTO::new);
        }
        return customerRepository.findByUserIsCurrentUserOrAvailable(pageable).map(CustomerDTO::new);
    }


    /**
     * Get one customer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Customer> findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            return customerRepository.findById(id);
        }
        return customerRepository.findByIdAndUserIsCurrentUser(id);
    }

    /**
     * Get one customer by NIP.
     *
     * @param nip the NIP of the customer
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Page<CustomerDTO> searchByNip(String nip, Pageable pageable) {
        return customerRepository.findByNip(nip, pageable)
            .map(customer -> new CustomerDTO(customer, isAdminOrAssigned(customer)));
    }

    /**
     * Delete the customer by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Customer : {}", id);        customerRepository.deleteById(id);
        customerSearchRepository.deleteById(id);
    }

    /**
     * Search for the customer corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Customer> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Customers for query {}", query);
        return customerSearchRepository.search(queryStringQuery(query), pageable);    }


    /**
     * Temporarily taken customers are released to available state after 90 days
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void releaseTemporarilyTakenCustomers() {
        customerRepository
            .findByStateAndCreatedDateBefore(CustomerState.TEMPORARILY_TAKEN, Instant.now().minus(90, ChronoUnit.DAYS))
            .forEach(customer -> {
                log.debug("Releasing not temporarily taken customer with NIP {}", customer.getNip());
                customer.setState(CustomerState.AVAILABLE);
                customerRepository.save(customer);
            });
    }

    private boolean isAdminOrAssigned(Customer customer) {
        final boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
        final boolean isAssigned = customer.getUser().equals(getUser());
        return isAssigned || isAdmin;
    }
}
