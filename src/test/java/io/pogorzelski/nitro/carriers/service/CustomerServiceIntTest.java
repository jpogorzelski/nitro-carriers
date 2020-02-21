package io.pogorzelski.nitro.carriers.service;

import io.pogorzelski.nitro.carriers.NitroCarriersApp;
import io.pogorzelski.nitro.carriers.domain.Customer;
import io.pogorzelski.nitro.carriers.domain.enumeration.CustomerState;
import io.pogorzelski.nitro.carriers.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NitroCarriersApp.class)
@Transactional
public class CustomerServiceIntTest {

    @Autowired
    private AuditingHandler auditingHandler;

    @Mock
    DateTimeProvider dateTimeProvider;


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    private Customer customer;

    @Before
    public void init() {
        customer = new Customer()
            .name("Customer name")
            .nip("542132132")
            .address("Testowa 12")
            .postalCode("12-123")
            .state(CustomerState.TAKEN)
            .notes("Notesss");
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now()));
        auditingHandler.setDateTimeProvider(dateTimeProvider);
    }


    @Test
    @Transactional
    public void testReleaseTemporarilyTakenCustomersAfter90Days() {
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(Instant.now().minus(90, ChronoUnit.DAYS)));

        customer.setState(CustomerState.TEMPORARILY_TAKEN);
        final Customer saved = customerRepository.saveAndFlush(this.customer);

        assertThat(customerRepository.findById(saved.getId()).get().getState()).isEqualTo(CustomerState.TEMPORARILY_TAKEN);
        customerService.releaseTemporarilyTakenCustomers();
        assertThat(customerRepository.findById(saved.getId()).get().getState()).isEqualTo(CustomerState.AVAILABLE);
    }

    @Test
    @Transactional
    public void testShouldNotReleaseCustomersIfStateIsTaken() {
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(Instant.now().minus(90, ChronoUnit.DAYS)));

        customer.setState(CustomerState.TAKEN);
        final Customer saved = customerRepository.saveAndFlush(this.customer);

        assertThat(customerRepository.findById(saved.getId()).get().getState()).isEqualTo(CustomerState.TAKEN);
        customerService.releaseTemporarilyTakenCustomers();
        assertThat(customerRepository.findById(saved.getId()).get().getState()).isEqualTo(CustomerState.TAKEN);
    }

    @Test
    @Transactional
    public void testShouldNotReleaseTemporarilyTakenCustomersBefore90Days() {
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(Instant.now().minus(89, ChronoUnit.DAYS)));

        customer.setState(CustomerState.TEMPORARILY_TAKEN);
        final Customer saved = customerRepository.saveAndFlush(this.customer);

        assertThat(customerRepository.findById(saved.getId()).get().getState()).isEqualTo(CustomerState.TEMPORARILY_TAKEN);
        customerService.releaseTemporarilyTakenCustomers();
        assertThat(customerRepository.findById(saved.getId()).get().getState()).isEqualTo(CustomerState.TEMPORARILY_TAKEN);
    }
}
