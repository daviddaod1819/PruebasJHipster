package com.mycompany.myapp.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.UserInfo} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.UserInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserInfoCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter sex;

    private InstantFilter birthDate;

    private StringFilter country;

    private StringFilter town;

    private StringFilter postCode;

    private LongFilter spacesId;

    public UserInfoCriteria() {}

    public UserInfoCriteria(UserInfoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sex = other.sex == null ? null : other.sex.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.town = other.town == null ? null : other.town.copy();
        this.postCode = other.postCode == null ? null : other.postCode.copy();
        this.spacesId = other.spacesId == null ? null : other.spacesId.copy();
    }

    @Override
    public UserInfoCriteria copy() {
        return new UserInfoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getSex() {
        return sex;
    }

    public void setSex(BooleanFilter sex) {
        this.sex = sex;
    }

    public InstantFilter getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(InstantFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getTown() {
        return town;
    }

    public void setTown(StringFilter town) {
        this.town = town;
    }

    public StringFilter getPostCode() {
        return postCode;
    }

    public void setPostCode(StringFilter postCode) {
        this.postCode = postCode;
    }

    public LongFilter getSpacesId() {
        return spacesId;
    }

    public void setSpacesId(LongFilter spacesId) {
        this.spacesId = spacesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserInfoCriteria that = (UserInfoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sex, that.sex) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(country, that.country) &&
            Objects.equals(town, that.town) &&
            Objects.equals(postCode, that.postCode) &&
            Objects.equals(spacesId, that.spacesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sex, birthDate, country, town, postCode, spacesId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserInfoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sex != null ? "sex=" + sex + ", " : "") +
                (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (town != null ? "town=" + town + ", " : "") +
                (postCode != null ? "postCode=" + postCode + ", " : "") +
                (spacesId != null ? "spacesId=" + spacesId + ", " : "") +
            "}";
    }
}
