package io.pogorzelski.nitro.carriers.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import io.pogorzelski.nitro.carriers.domain.enumeration.CargoType;

import io.pogorzelski.nitro.carriers.domain.enumeration.Grade;

/**
 * A Rating.
 */
@Entity
@Table(name = "rating")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rating")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "charge_postal_code", nullable = false)
    private String chargePostalCode;

    @NotNull
    @Column(name = "discharge_postal_code", nullable = false)
    private String dischargePostalCode;

    @Column(name = "add_alternative")
    private Boolean addAlternative;

    @NotNull
    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "price_per_km", precision = 10, scale = 2)
    private BigDecimal pricePerKm;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo_type", nullable = false)
    private CargoType cargoType;

    @NotNull
    @Column(name = "distance", nullable = false)
    private Double distance;

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

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "white_list")
    private Boolean whiteList;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("ratings")
    private Carrier carrier;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("ratings")
    private Person person;

    @ManyToOne
    @JsonIgnoreProperties("ratings")
    private Carrier altCarrier;

    @ManyToOne
    @JsonIgnoreProperties("ratings")
    private Person altPerson;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("ratings")
    private Country chargeCountry;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("ratings")
    private City chargeCity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("ratings")
    private Country dischargeCountry;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("ratings")
    private City dischargeCity;

    @ManyToOne
    @JsonIgnoreProperties("ratings")
    private User createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChargePostalCode() {
        return chargePostalCode;
    }

    public Rating chargePostalCode(String chargePostalCode) {
        this.chargePostalCode = chargePostalCode;
        return this;
    }

    public void setChargePostalCode(String chargePostalCode) {
        this.chargePostalCode = chargePostalCode;
    }

    public String getDischargePostalCode() {
        return dischargePostalCode;
    }

    public Rating dischargePostalCode(String dischargePostalCode) {
        this.dischargePostalCode = dischargePostalCode;
        return this;
    }

    public void setDischargePostalCode(String dischargePostalCode) {
        this.dischargePostalCode = dischargePostalCode;
    }

    public Boolean isAddAlternative() {
        return addAlternative;
    }

    public Rating addAlternative(Boolean addAlternative) {
        this.addAlternative = addAlternative;
        return this;
    }

    public void setAddAlternative(Boolean addAlternative) {
        this.addAlternative = addAlternative;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Rating totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getPricePerKm() {
        return pricePerKm;
    }

    public Rating pricePerKm(BigDecimal pricePerKm) {
        this.pricePerKm = pricePerKm;
        return this;
    }

    public void setPricePerKm(BigDecimal pricePerKm) {
        this.pricePerKm = pricePerKm;
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

    public Double getDistance() {
        return distance;
    }

    public Rating distance(Double distance) {
        this.distance = distance;
        return this;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
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

    public String getRemarks() {
        return remarks;
    }

    public Rating remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean isWhiteList() {
        return whiteList;
    }

    public Rating whiteList(Boolean whiteList) {
        this.whiteList = whiteList;
        return this;
    }

    public void setWhiteList(Boolean whiteList) {
        this.whiteList = whiteList;
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

    public Carrier getAltCarrier() {
        return altCarrier;
    }

    public Rating altCarrier(Carrier carrier) {
        this.altCarrier = carrier;
        return this;
    }

    public void setAltCarrier(Carrier carrier) {
        this.altCarrier = carrier;
    }

    public Person getAltPerson() {
        return altPerson;
    }

    public Rating altPerson(Person person) {
        this.altPerson = person;
        return this;
    }

    public void setAltPerson(Person person) {
        this.altPerson = person;
    }

    public Country getChargeCountry() {
        return chargeCountry;
    }

    public Rating chargeCountry(Country country) {
        this.chargeCountry = country;
        return this;
    }

    public void setChargeCountry(Country country) {
        this.chargeCountry = country;
    }

    public City getChargeCity() {
        return chargeCity;
    }

    public Rating chargeCity(City city) {
        this.chargeCity = city;
        return this;
    }

    public void setChargeCity(City city) {
        this.chargeCity = city;
    }

    public Country getDischargeCountry() {
        return dischargeCountry;
    }

    public Rating dischargeCountry(Country country) {
        this.dischargeCountry = country;
        return this;
    }

    public void setDischargeCountry(Country country) {
        this.dischargeCountry = country;
    }

    public City getDischargeCity() {
        return dischargeCity;
    }

    public Rating dischargeCity(City city) {
        this.dischargeCity = city;
        return this;
    }

    public void setDischargeCity(City city) {
        this.dischargeCity = city;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Rating createdBy(User user) {
        this.createdBy = user;
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
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
            ", chargePostalCode='" + getChargePostalCode() + "'" +
            ", dischargePostalCode='" + getDischargePostalCode() + "'" +
            ", addAlternative='" + isAddAlternative() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", pricePerKm=" + getPricePerKm() +
            ", cargoType='" + getCargoType() + "'" +
            ", distance=" + getDistance() +
            ", contact=" + getContact() +
            ", price=" + getPrice() +
            ", flexibility=" + getFlexibility() +
            ", recommendation='" + getRecommendation() + "'" +
            ", average=" + getAverage() +
            ", remarks='" + getRemarks() + "'" +
            ", whiteList='" + isWhiteList() + "'" +
            "}";
    }
}
