package io.pogorzelski.nitro.carriers.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @NotNull
    @Column(name = "country_name_pl", nullable = false)
    private String countryNamePL;

    @NotNull
    @Column(name = "country_name_en", nullable = false)
    private String countryNameEN;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Country countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryNamePL() {
        return countryNamePL;
    }

    public Country countryNamePL(String countryNamePL) {
        this.countryNamePL = countryNamePL;
        return this;
    }

    public void setCountryNamePL(String countryNamePL) {
        this.countryNamePL = countryNamePL;
    }

    public String getCountryNameEN() {
        return countryNameEN;
    }

    public Country countryNameEN(String countryNameEN) {
        this.countryNameEN = countryNameEN;
        return this;
    }

    public void setCountryNameEN(String countryNameEN) {
        this.countryNameEN = countryNameEN;
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
        Country country = (Country) o;
        if (country.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), country.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", countryCode='" + getCountryCode() + "'" +
            ", countryNamePL='" + getCountryNamePL() + "'" +
            ", countryNameEN='" + getCountryNameEN() + "'" +
            "}";
    }
}
