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
 * Criteria class for the Survivor entity. This class is used in SurvivorResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /survivors?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SurvivorCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private IntegerFilter age;

    private LongFilter lastLocationId;

    private LongFilter inventoryId;

    private LongFilter reportedId;

    public SurvivorCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getAge() {
        return age;
    }

    public void setAge(IntegerFilter age) {
        this.age = age;
    }

    public LongFilter getLastLocationId() {
        return lastLocationId;
    }

    public void setLastLocationId(LongFilter lastLocationId) {
        this.lastLocationId = lastLocationId;
    }

    public LongFilter getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(LongFilter inventoryId) {
        this.inventoryId = inventoryId;
    }

    public LongFilter getReportedId() {
        return reportedId;
    }

    public void setReportedId(LongFilter reportedId) {
        this.reportedId = reportedId;
    }

    @Override
    public String toString() {
        return "SurvivorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (age != null ? "age=" + age + ", " : "") +
                (lastLocationId != null ? "lastLocationId=" + lastLocationId + ", " : "") +
                (inventoryId != null ? "inventoryId=" + inventoryId + ", " : "") +
                (reportedId != null ? "reportedId=" + reportedId + ", " : "") +
            "}";
    }

}
