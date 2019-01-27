package io.pogorzelski.nitro.carriers.service.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import io.pogorzelski.nitro.carriers.domain.enumeration.Grade;

public class RatingExtDTO {


    private Long id;


    private Integer carrierTransId;

    private String carrierName;

    private Integer personTransId;

    private String personFirstName;

    private String personLastName;

    private String chargeAddressCountry;

    private String chargeAddressPostalCode;

    private String dischargeAddressCountry;

    private String dischargeAddressPostalCode;

    private Long cargoTypeId;

    private String cargoTypeName;

    @Min(value = 1)
    @Max(value = 6)
    private Integer contact;

    @Min(value = 1)
    @Max(value = 6)
    private Integer price;

    @Min(value = 1)
    @Max(value = 6)
    private Integer flexibiliy;

    private Grade recommendation;

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

    public Integer getFlexibiliy() {
        return flexibiliy;
    }

    public void setFlexibiliy(final Integer flexibiliy) {
        this.flexibiliy = flexibiliy;
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
}
