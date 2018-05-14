package com.github.netstart.zssn.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the ContaminationFlag entity. This class is used in ContaminationFlagResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contamination-flags?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContaminationFlagCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter reportedById;

    private LongFilter reportedId;

    public ContaminationFlagCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getReportedById() {
        return reportedById;
    }

    public void setReportedById(LongFilter reportedById) {
        this.reportedById = reportedById;
    }

    public LongFilter getReportedId() {
        return reportedId;
    }

    public void setReportedId(LongFilter reportedId) {
        this.reportedId = reportedId;
    }

    @Override
    public String toString() {
        return "ContaminationFlagCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (reportedById != null ? "reportedById=" + reportedById + ", " : "") +
                (reportedId != null ? "reportedId=" + reportedId + ", " : "") +
            "}";
    }

}
