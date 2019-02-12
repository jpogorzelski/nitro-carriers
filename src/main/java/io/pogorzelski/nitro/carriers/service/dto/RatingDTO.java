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

    @NotNull
    private String chargePostalCode;

    @NotNull
    private String dischargePostalCode;

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

    private Double average;


    private Long carrierId;

    private String carrierName;

    private Long personId;

    private String personFirstName;

    private Long chargeCountryId;

    private String chargeCountryCountryName;

    private Long dischargeCountryId;

    private String dischargeCountryCountryName;

    private Long cargoTypeId;

    private String cargoTypeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChargePostalCode() {
        return chargePostalCode;
    }

    public void setChargePostalCode(String chargePostalCode) {
        this.chargePostalCode = chargePostalCode;
    }

    public String getDischargePostalCode() {
        return dischargePostalCode;
    }

    public void setDischargePostalCode(String dischargePostalCode) {
        this.dischargePostalCode = dischargePostalCode;
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

    public Integer getFlexibility() {
        return flexibility;
    }

    public void setFlexibility(Integer flexibility) {
        this.flexibility = flexibility;
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

    public Long getChargeCountryId() {
        return chargeCountryId;
    }

    public void setChargeCountryId(Long countryId) {
        this.chargeCountryId = countryId;
    }

    public String getChargeCountryCountryName() {
        return chargeCountryCountryName;
    }

    public void setChargeCountryCountryName(String countryCountryName) {
        this.chargeCountryCountryName = countryCountryName;
    }

    public Long getDischargeCountryId() {
        return dischargeCountryId;
    }

    public void setDischargeCountryId(Long countryId) {
        this.dischargeCountryId = countryId;
    }

    public String getDischargeCountryCountryName() {
        return dischargeCountryCountryName;
    }

    public void setDischargeCountryCountryName(String countryCountryName) {
        this.dischargeCountryCountryName = countryCountryName;
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
            ", chargePostalCode='" + getChargePostalCode() + "'" +
            ", dischargePostalCode='" + getDischargePostalCode() + "'" +
            ", contact=" + getContact() +
            ", price=" + getPrice() +
            ", flexibility=" + getFlexibility() +
            ", recommendation='" + getRecommendation() + "'" +
            ", average=" + getAverage() +
            ", carrier=" + getCarrierId() +
            ", carrier='" + getCarrierName() + "'" +
            ", person=" + getPersonId() +
            ", person='" + getPersonFirstName() + "'" +
            ", chargeCountry=" + getChargeCountryId() +
            ", chargeCountry='" + getChargeCountryCountryName() + "'" +
            ", dischargeCountry=" + getDischargeCountryId() +
            ", dischargeCountry='" + getDischargeCountryCountryName() + "'" +
            ", cargoType=" + getCargoTypeId() +
            ", cargoType='" + getCargoTypeName() + "'" +
            "}";
    }
}
