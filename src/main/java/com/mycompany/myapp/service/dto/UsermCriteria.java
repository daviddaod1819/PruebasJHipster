package com.mycompany.myapp.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Userm} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.UsermResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /userms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UsermCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userInfoId;

    public UsermCriteria() {}

    public UsermCriteria(UsermCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userInfoId = other.userInfoId == null ? null : other.userInfoId.copy();
    }

    @Override
    public UsermCriteria copy() {
        return new UsermCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(LongFilter userInfoId) {
        this.userInfoId = userInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UsermCriteria that = (UsermCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(userInfoId, that.userInfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userInfoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsermCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userInfoId != null ? "userInfoId=" + userInfoId + ", " : "") +
            "}";
    }
}
