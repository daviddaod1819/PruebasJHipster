package com.mycompany.myapp.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Space} entity.
 */
public class SpaceDTO implements Serializable {
    private Long id;

    private Integer rooms;

    private Integer meters;

    private Integer price;

    private String details;

    private Long userInfoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getMeters() {
        return meters;
    }

    public void setMeters(Integer meters) {
        this.meters = meters;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpaceDTO)) {
            return false;
        }

        return id != null && id.equals(((SpaceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpaceDTO{" +
            "id=" + getId() +
            ", rooms=" + getRooms() +
            ", meters=" + getMeters() +
            ", price=" + getPrice() +
            ", details='" + getDetails() + "'" +
            ", userInfoId=" + getUserInfoId() +
            "}";
    }
}
