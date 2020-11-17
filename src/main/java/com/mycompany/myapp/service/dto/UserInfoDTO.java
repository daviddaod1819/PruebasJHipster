package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.UserInfo} entity.
 */
public class UserInfoDTO implements Serializable {
    private Long id;

    private Boolean sex;

    @NotNull
    private Instant birthDate;

    @Size(min = 1, max = 50)
    private String country;

    @Size(min = 1, max = 50)
    private String town;

    @Size(min = 5, max = 5)
    private String postCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserInfoDTO)) {
            return false;
        }

        return id != null && id.equals(((UserInfoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserInfoDTO{" +
            "id=" + getId() +
            ", sex='" + isSex() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", country='" + getCountry() + "'" +
            ", town='" + getTown() + "'" +
            ", postCode='" + getPostCode() + "'" +
            "}";
    }
}
