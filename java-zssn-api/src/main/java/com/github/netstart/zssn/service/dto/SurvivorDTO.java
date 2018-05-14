package com.github.netstart.zssn.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Survivor entity.
 */
public class SurvivorDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer age;

    private Long lastLocationId;

    private Long inventoryId;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getLastLocationId() {
        return lastLocationId;
    }

    public void setLastLocationId(Long locationId) {
        this.lastLocationId = locationId;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SurvivorDTO survivorDTO = (SurvivorDTO) o;
        if(survivorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), survivorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SurvivorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            "}";
    }
}
