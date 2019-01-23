package io.pogorzelski.nitro.carriers.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import io.pogorzelski.nitro.carriers.domain.enumeration.Grade;

/**
 * A DTO for the Rating entity.
 */
public class RatingDTO implements Serializable {

    private Long id;

    @Min(value = 1)
    @Max(value = 6)
    private Integer flexibiliy;

    @Min(value = 1)
    @Max(value = 6)
    private Integer contact;

    @Min(value = 1)
    @Max(value = 6)
    private Integer price;

    private Grade recommendation;

    @DecimalMin(value = "1")
    @DecimalMax(value = "6")
    private Double average;

    private Long personId;

    private String personFirstName;

    private Long chargeAddressId;

    private String chargeAddressPostalCode;

    private Long dischargeAddressId;

    private String dischargeAddressPostalCode;

    private Long cargoTypeId;

    private String cargoTypeName;

    private Long carrierId;

    private String carrierName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFlexibiliy() {
        return flexibiliy;
    }

    public void setFlexibiliy(Integer flexibiliy) {
        this.flexibiliy = flexibiliy;
    }

    public Integer getContact() {
        return contact;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Grade getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Grade recommendation) {
        this.recommendation = recommendation;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public Long getChargeAddressId() {
        return chargeAddressId;
    }

    public void setChargeAddressId(Long addressId) {
        this.chargeAddressId = addressId;
    }

    public String getChargeAddressPostalCode() {
        return chargeAddressPostalCode;
    }

    public void setChargeAddressPostalCode(String addressPostalCode) {
        this.chargeAddressPostalCode = addressPostalCode;
    }

    public Long getDischargeAddressId() {
        return dischargeAddressId;
    }

    public void setDischargeAddressId(Long addressId) {
        this.dischargeAddressId = addressId;
    }

    public String getDischargeAddressPostalCode() {
        return dischargeAddressPostalCode;
    }

    public void setDischargeAddressPostalCode(String addressPostalCode) {
        this.dischargeAddressPostalCode = addressPostalCode;
    }

    public Long getCargoTypeId() {
        return cargoTypeId;
    }

    public void setCargoTypeId(Long cargoTypeId) {
        this.cargoTypeId = cargoTypeId;
    }

    public String getCargoTypeName() {
        return cargoTypeName;
    }

    public void setCargoTypeName(String cargoTypeName) {
        this.cargoTypeName = cargoTypeName;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Long carrierId) {
        this.carrierId = carrierId;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RatingDTO ratingDTO = (RatingDTO) o;
        if (ratingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ratingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RatingDTO{" +
            "id=" + getId() +
            ", flexibiliy=" + getFlexibiliy() +
            ", contact=" + getContact() +
            ", price=" + getPrice() +
            ", recommendation='" + getRecommendation() + "'" +
            ", average=" + getAverage() +
            ", person=" + getPersonId() +
            ", person='" + getPersonFirstName() + "'" +
            ", chargeAddress=" + getChargeAddressId() +
            ", chargeAddress='" + getChargeAddressPostalCode() + "'" +
            ", dischargeAddress=" + getDischargeAddressId() +
            ", dischargeAddress='" + getDischargeAddressPostalCode() + "'" +
            ", cargoType=" + getCargoTypeId() +
            ", cargoType='" + getCargoTypeName() + "'" +
            ", carrier=" + getCarrierId() +
            ", carrier='" + getCarrierName() + "'" +
            "}";
    }
}
