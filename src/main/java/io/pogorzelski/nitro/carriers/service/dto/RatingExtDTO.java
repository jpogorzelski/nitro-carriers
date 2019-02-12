package io.pogorzelski.nitro.carriers.service.dto;

import javax.validation.constraints.*;

import io.pogorzelski.nitro.carriers.domain.enumeration.Grade;

import java.io.Serializable;
import java.util.Objects;

public class RatingExtDTO implements Serializable {

    private Long id;

    private Integer carrierTransId;

    private String carrierName;

    private Integer personTransId;

    private String personFirstName;

    private String personLastName;

    private String chargeAddressCountry;

    @NotNull
    private String chargeAddressPostalCode;

    private String dischargeAddressCountry;

    @NotNull
    private String dischargeAddressPostalCode;

    private Long cargoTypeId;

    private String cargoTypeName;

    @NotNull
    @Min(value = 1)
    @Max(value = 6)
    private Integer contact;

    @NotNull
    @Min(value = 1)
    @Max(value = 6)
    private Integer price;

    @NotNull
    @Min(value = 1)
    @Max(value = 6)
    private Integer flexibility;

    @NotNull
    private Grade recommendation;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "6")
    private Double average;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getCarrierTransId() {
        return carrierTransId;
    }

    public void setCarrierTransId(final Integer carrierTransId) {
        this.carrierTransId = carrierTransId;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(final String carrierName) {
        this.carrierName = carrierName;
    }

    public Integer getPersonTransId() {
        return personTransId;
    }

    public void setPersonTransId(final Integer personTransId) {
        this.personTransId = personTransId;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(final String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(final String personLastName) {
        this.personLastName = personLastName;
    }

    public String getChargeAddressCountry() {
        return chargeAddressCountry;
    }

    public void setChargeAddressCountry(final String chargeAddressCountry) {
        this.chargeAddressCountry = chargeAddressCountry;
    }

    public String getChargeAddressPostalCode() {
        return chargeAddressPostalCode;
    }

    public void setChargeAddressPostalCode(final String chargeAddressPostalCode) {
        this.chargeAddressPostalCode = chargeAddressPostalCode;
    }

    public String getDischargeAddressCountry() {
        return dischargeAddressCountry;
    }

    public void setDischargeAddressCountry(final String dischargeAddressCountry) {
        this.dischargeAddressCountry = dischargeAddressCountry;
    }

    public String getDischargeAddressPostalCode() {
        return dischargeAddressPostalCode;
    }

    public void setDischargeAddressPostalCode(final String dischargeAddressPostalCode) {
        this.dischargeAddressPostalCode = dischargeAddressPostalCode;
    }

    public Long getCargoTypeId() {
        return cargoTypeId;
    }

    public void setCargoTypeId(final Long cargoTypeId) {
        this.cargoTypeId = cargoTypeId;
    }

    public String getCargoTypeName() {
        return cargoTypeName;
    }

    public void setCargoTypeName(final String cargoTypeName) {
        this.cargoTypeName = cargoTypeName;
    }

    public Integer getContact() {
        return contact;
    }

    public void setContact(final Integer contact) {
        this.contact = contact;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }

    public Integer getFlexibility() {
        return flexibility;
    }

    public void setFlexibility(final Integer flexibility) {
        this.flexibility = flexibility;
    }

    public Grade getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(final Grade recommendation) {
        this.recommendation = recommendation;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(final Double average) {
        this.average = average;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RatingExtDTO ratingExtDTO = (RatingExtDTO) o;
        if (ratingExtDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ratingExtDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RatingExtDTO{" +
            "contact=" + contact +
            ", price=" + price +
            ", flexibility=" + flexibility +
            ", recommendation=" + recommendation +
            ", average=" + average +
            ", id=" + id +
            ", carrierTransId=" + carrierTransId +
            ", carrierName='" + carrierName + '\'' +
            ", personTransId=" + personTransId +
            ", personFirstName='" + personFirstName + '\'' +
            ", personLastName='" + personLastName + '\'' +
            ", chargeAddressCountry='" + chargeAddressCountry + '\'' +
            ", chargeAddressPostalCode='" + chargeAddressPostalCode + '\'' +
            ", dischargeAddressCountry='" + dischargeAddressCountry + '\'' +
            ", dischargeAddressPostalCode='" + dischargeAddressPostalCode + '\'' +
            ", cargoTypeId=" + cargoTypeId +
            ", cargoTypeName='" + cargoTypeName + '\'' +
            '}';
    }
}
