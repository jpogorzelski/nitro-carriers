package io.pogorzelski.nitro.carriers.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.pogorzelski.nitro.carriers.domain.City;
import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.domain.Customer;
import io.pogorzelski.nitro.carriers.domain.User;
import io.pogorzelski.nitro.carriers.domain.enumeration.CustomerState;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A DTO representing a customer with permission flag
 */
public class CustomerDTO {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String nip;

    private String address;

    private String postalCode;

    private CustomerState state;

    private String notes;

    @JsonIgnoreProperties("customers")
    private City city;

    @JsonIgnoreProperties("customers")
    private Country country;

    @JsonIgnoreProperties("customers")
    private User user;

    private boolean canModify;

    public CustomerDTO() {

    }

    public CustomerDTO(Customer customer) {
        this(customer, true);

    }

    public CustomerDTO(Customer customer, boolean canModify) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.nip = customer.getNip();
        this.address = customer.getAddress();
        this.postalCode = customer.getPostalCode();
        this.state = customer.getState();
        this.notes = customer.getNotes();
        this.city = customer.getCity();
        this.country = customer.getCountry();
        this.user = customer.getUser();
        this.canModify = canModify;
    }

    public CustomerDTO(Long id, @NotNull String name, @NotNull String nip, String address, String postalCode, CustomerState state, String notes, City city, Country country, User user, boolean canModify) {
        this.id = id;
        this.name = name;
        this.nip = nip;
        this.address = address;
        this.postalCode = postalCode;
        this.state = state;
        this.notes = notes;
        this.city = city;
        this.country = country;
        this.user = user;
        this.canModify = canModify;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public CustomerState getState() {
        return state;
    }

    public void setState(CustomerState state) {
        this.state = state;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCanModify() {
        return canModify;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDTO that = (CustomerDTO) o;
        return canModify == that.canModify &&
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(nip, that.nip) &&
            Objects.equals(address, that.address) &&
            Objects.equals(postalCode, that.postalCode) &&
            state == that.state &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country) &&
            Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nip, address, postalCode, state, notes, city, country, user, canModify);
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", nip='" + nip + '\'' +
            ", address='" + address + '\'' +
            ", postalCode='" + postalCode + '\'' +
            ", state=" + state +
            ", notes='" + notes + '\'' +
            ", city=" + city +
            ", country=" + country +
            ", user=" + user +
            ", canModify=" + canModify +
            '}';
    }
}
