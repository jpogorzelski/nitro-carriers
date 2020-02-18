package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.domain.Customer;
import io.pogorzelski.nitro.carriers.domain.User;
import io.pogorzelski.nitro.carriers.repository.CustomerRepository;
import io.pogorzelski.nitro.carriers.repository.search.CustomerSearchRepository;
import io.pogorzelski.nitro.carriers.security.AuthoritiesConstants;
import io.pogorzelski.nitro.carriers.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final CustomerSearchRepository customerSearchRepository;

    public CustomerService(CustomerRepository customerRepository, UserService userService, CustomerSearchRepository customerSearchRepository) {
        this.customerRepository = customerRepository;
        this.userService = userService;
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
        if (customer.getUser() == null) {
            customer.setUser(loggedInUser);
        } else if (!customer.getUser().equals(getUser())) {
            log.error("User not allowed to save customer={}", customer);
            return null;
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
    public Page<Customer> findAll(Pageable pageable) {
        log.debug("Request to get all Customers");
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            return customerRepository.findAll(pageable);
        }
        return customerRepository.findByUserIsCurrentUserOrAvailable(pageable);
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
}
