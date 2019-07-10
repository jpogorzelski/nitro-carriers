package io.pogorzelski.nitro.carriers.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Carrier.
 */
@Entity
@Table(name = "carrier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Carrier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "trans_id")
    private Integer transId;

    @Column(name = "acronym")
    private String acronym;

    @Column(name = "nip")
    private String nip;

    @OneToMany(mappedBy = "carrier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Person> people = new HashSet<>();

    @OneToMany(mappedBy = "carrier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Rating> ratings = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Carrier name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTransId() {
        return transId;
    }

    public Carrier transId(Integer transId) {
        this.transId = transId;
        return this;
    }

    public void setTransId(Integer transId) {
        this.transId = transId;
    }

    public String getAcronym() {
        return acronym;
    }

    public Carrier acronym(String acronym) {
        this.acronym = acronym;
        return this;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getNip() {
        return nip;
    }

    public Carrier nip(String nip) {
        this.nip = nip;
        return this;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public Set<Person> getPeople() {
        return people;
    }

    public Carrier people(Set<Person> people) {
        this.people = people;
        return this;
    }

    public Carrier addPeople(Person person) {
        this.people.add(person);
        person.setCarrier(this);
        return this;
    }

    public Carrier removePeople(Person person) {
        this.people.remove(person);
        person.setCarrier(null);
        return this;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public Carrier ratings(Set<Rating> ratings) {
        this.ratings = ratings;
        return this;
    }

    public Carrier addRatings(Rating rating) {
        this.ratings.add(rating);
        rating.setCarrier(this);
        return this;
    }

    public Carrier removeRatings(Rating rating) {
        this.ratings.remove(rating);
        rating.setCarrier(null);
        return this;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
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
        Carrier carrier = (Carrier) o;
        if (carrier.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), carrier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Carrier{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", transId=" + getTransId() +
            ", acronym='" + getAcronym() + "'" +
            ", nip='" + getNip() + "'" +
            "}";
    }
}
