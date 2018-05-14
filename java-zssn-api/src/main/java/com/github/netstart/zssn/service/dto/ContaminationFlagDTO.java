package com.github.netstart.zssn.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ContaminationFlag entity.
 */
public class ContaminationFlagDTO implements Serializable {

    private Long id;

    private Long reportedById;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportedById() {
        return reportedById;
    }

    public void setReportedById(Long survivorId) {
        this.reportedById = survivorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContaminationFlagDTO contaminationFlagDTO = (ContaminationFlagDTO) o;
        if(contaminationFlagDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contaminationFlagDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContaminationFlagDTO{" +
            "id=" + getId() +
            "}";
    }
}
