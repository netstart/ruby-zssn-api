package com.github.netstart.zssn.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ContaminationFlag.
 */
@Entity
@Table(name = "contamination_flag")
public class ContaminationFlag implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@NotNull
	private Survivor reportedBy;

	@ManyToOne(optional = false)
	@NotNull
	private Survivor reported;

	@Deprecated
	public ContaminationFlag() {
	}

	public ContaminationFlag(Survivor reported, Survivor survivorReporter) {
		this.reported = reported;
		this.reportedBy = survivorReporter;
	}

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Survivor getReportedBy() {
		return reportedBy;
	}

	public ContaminationFlag reportedBy(Survivor survivor) {
		this.reportedBy = survivor;
		return this;
	}

	public void setReportedBy(Survivor survivor) {
		this.reportedBy = survivor;
	}

	public Survivor getReported() {
		return reported;
	}

	public ContaminationFlag reported(Survivor survivor) {
		this.reported = survivor;
		return this;
	}

	public void setReported(Survivor survivor) {
		this.reported = survivor;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ContaminationFlag contaminationFlag = (ContaminationFlag) o;
		if (contaminationFlag.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), contaminationFlag.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "ContaminationFlag{" + "id=" + getId() + "}";
	}
}
