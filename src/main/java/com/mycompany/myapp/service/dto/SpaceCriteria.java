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
 * Criteria class for the {@link com.mycompany.myapp.domain.Space} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SpaceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /spaces?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SpaceCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter rooms;

    private IntegerFilter meters;

    private IntegerFilter price;

    private StringFilter details;

    private LongFilter userInfoId;

    public SpaceCriteria() {}

    public SpaceCriteria(SpaceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rooms = other.rooms == null ? null : other.rooms.copy();
        this.meters = other.meters == null ? null : other.meters.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.details = other.details == null ? null : other.details.copy();
        this.userInfoId = other.userInfoId == null ? null : other.userInfoId.copy();
    }

    @Override
    public SpaceCriteria copy() {
        return new SpaceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getRooms() {
        return rooms;
    }

    public void setRooms(IntegerFilter rooms) {
        this.rooms = rooms;
    }

    public IntegerFilter getMeters() {
        return meters;
    }

    public void setMeters(IntegerFilter meters) {
        this.meters = meters;
    }

    public IntegerFilter getPrice() {
        return price;
    }

    public void setPrice(IntegerFilter price) {
        this.price = price;
    }

    public StringFilter getDetails() {
        return details;
    }

    public void setDetails(StringFilter details) {
        this.details = details;
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
        final SpaceCriteria that = (SpaceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rooms, that.rooms) &&
            Objects.equals(meters, that.meters) &&
            Objects.equals(price, that.price) &&
            Objects.equals(details, that.details) &&
            Objects.equals(userInfoId, that.userInfoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rooms, meters, price, details, userInfoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpaceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (rooms != null ? "rooms=" + rooms + ", " : "") +
                (meters != null ? "meters=" + meters + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (details != null ? "details=" + details + ", " : "") +
                (userInfoId != null ? "userInfoId=" + userInfoId + ", " : "") +
            "}";
    }
}
