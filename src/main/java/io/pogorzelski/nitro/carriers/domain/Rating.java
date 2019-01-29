package io.pogorzelski.nitro.carriers.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import io.pogorzelski.nitro.carriers.domain.enumeration.Grade;

/**
 * A Rating.
 */
@Entity
@Table(name = "rating")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 6)
    @Column(name = "contact", nullable = false)
    private Integer contact;

    @NotNull
    @Min(value = 1)
    @Max(value = 6)
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Min(value = 1)
    @Max(value = 6)
    @Column(name = "flexibility", nullable = false)
    private Integer flexibility;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation", nullable = false)
    private Grade recommendation;

    @Column(name = "average")
    private Double average;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @NotNull
    @JsonIgnoreProperties("ratings")
    private Carrier carrier;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @NotNull
    @JsonIgnoreProperties("ratings")
    private Person person;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @NotNull
    @JsonIgnoreProperties("ratings")
    private Address chargeAddress;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @NotNull
    @JsonIgnoreProperties("ratings")
    private Address dischargeAddress;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("ratings")
    private CargoType cargoType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getContact() {
        return contact;
    }

    public Rating contact(Integer contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
    }

    public Integer getPrice() {
        return price;
    }

    public Rating price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getFlexibility() {
        return flexibility;
    }

    public Rating flexibility(Integer flexibility) {
        this.flexibility = flexibility;
        return this;
    }

    public void setFlexibility(Integer flexibility) {
        this.flexibility = flexibility;
    }

    public Grade getRecommendation() {
        return recommendation;
    }

    public Rating recommendation(Grade recommendation) {
        this.recommendation = recommendation;
        return this;
    }

    public void setRecommendation(Grade recommendation) {
        this.recommendation = recommendation;
    }

    public Double getAverage() {
        return average;
    }

    public Rating average(Double average) {
        this.average = average;
        return this;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public Rating carrier(Carrier carrier) {
        this.carrier = carrier;
        return this;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    public Person getPerson() {
        return person;
    }

    public Rating person(Person person) {
        this.person = person;
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Address getChargeAddress() {
        return chargeAddress;
    }

    public Rating chargeAddress(Address address) {
        this.chargeAddress = address;
        return this;
    }

    public void setChargeAddress(Address address) {
        this.chargeAddress = address;
    }

    public Address getDischargeAddress() {
        return dischargeAddress;
    }

    public Rating dischargeAddress(Address address) {
        this.dischargeAddress = address;
        return this;
    }

    public void setDischargeAddress(Address address) {
        this.dischargeAddress = address;
    }

    public CargoType getCargoType() {
        return cargoType;
    }

    public Rating cargoType(CargoType cargoType) {
        this.cargoType = cargoType;
        return this;
    }

    public void setCargoType(CargoType cargoType) {
        this.cargoType = cargoType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rating rating = (Rating) o;
        if (rating.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rating.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rating{" +
            "id=" + getId() +
            ", contact=" + getContact() +
            ", price=" + getPrice() +
            ", flexibility=" + getFlexibility() +
            ", recommendation='" + getRecommendation() + "'" +
            ", average=" + getAverage() +
            "}";
    }
}
