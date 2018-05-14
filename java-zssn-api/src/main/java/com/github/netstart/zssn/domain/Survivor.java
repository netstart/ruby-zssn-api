package com.github.netstart.zssn.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Survivor.
 */
@Entity
@Table(name = "survivor")
public class Survivor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "age", nullable = false)
    private Integer age;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Location lastLocation;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Inventory inventory;

    @OneToMany(mappedBy = "reported")
    @JsonIgnore
    private Set<ContaminationFlag> reporteds = new HashSet<>();

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

    public Survivor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public Survivor age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public Survivor lastLocation(Location location) {
        this.lastLocation = location;
        return this;
    }

    public void setLastLocation(Location location) {
        this.lastLocation = location;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Survivor inventory(Inventory inventory) {
        this.inventory = inventory;
        return this;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Set<ContaminationFlag> getReporteds() {
        return reporteds;
    }

    public Survivor reporteds(Set<ContaminationFlag> contaminationFlags) {
        this.reporteds = contaminationFlags;
        return this;
    }

    public Survivor addReported(ContaminationFlag contaminationFlag) {
        this.reporteds.add(contaminationFlag);
        contaminationFlag.setReported(this);
        return this;
    }

    public Survivor removeReported(ContaminationFlag contaminationFlag) {
        this.reporteds.remove(contaminationFlag);
        contaminationFlag.setReported(null);
        return this;
    }

    public void setReporteds(Set<ContaminationFlag> contaminationFlags) {
        this.reporteds = contaminationFlags;
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
        Survivor survivor = (Survivor) o;
        if (survivor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), survivor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Survivor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            "}";
    }
}
