package io.pogorzelski.nitro.carriers.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Carrier entity.
 */
public class CarrierDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer transId;


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

    public Integer getTransId() {
        return transId;
    }

    public void setTransId(Integer transId) {
        this.transId = transId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CarrierDTO carrierDTO = (CarrierDTO) o;
        if (carrierDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), carrierDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CarrierDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", transId=" + getTransId() +
            "}";
    }
}
