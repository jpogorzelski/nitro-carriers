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

    @Min(value = 1)
    @Max(value = 6)
    @Column(name = "flexibility")
    private Integer flexibility;

    @Min(value = 1)
    @Max(value = 6)
    @Column(name = "contact")
    private Integer contact;

    @Min(value = 1)
    @Max(value = 6)
    @Column(name = "price")
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation")
    private Grade recommendation;

    @DecimalMin(value = "1")
    @DecimalMax(value = "6")
    @Column(name = "average")
    private Double average;

    @OneToOne(optional = false)    @NotNull

    @JoinColumn(unique = true)
    private Person person;

    @OneToOne(optional = false)    @NotNull

    @JoinColumn(unique = true)
    private Address chargeAddress;

    @OneToOne(optional = false)    @NotNull

    @JoinColumn(unique = true)
    private Address dischargeAddress;

    @OneToOne(optional = false)    @NotNull

    @JoinColumn(unique = true)
    private CargoType cargoType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("ratings")
    private Carrier carrier;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
            ", flexibility=" + getFlexibility() +
            ", contact=" + getContact() +
            ", price=" + getPrice() +
            ", recommendation='" + getRecommendation() + "'" +
            ", average=" + getAverage() +
            "}";
    }
}
