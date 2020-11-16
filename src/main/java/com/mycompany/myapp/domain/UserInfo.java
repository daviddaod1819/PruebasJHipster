package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserInfo.
 */
@Entity
@Table(name = "user_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sex")
    private Boolean sex;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private Instant birthDate;

    @Size(min = 1, max = 50)
    @Column(name = "country", length = 50)
    private String country;

    @Size(min = 1, max = 50)
    @Column(name = "town", length = 50)
    private String town;

    @Size(min = 5, max = 5)
    @Column(name = "post_code", length = 5)
    private String postCode;

    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Space> spaces = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSex() {
        return sex;
    }

    public UserInfo sex(Boolean sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public UserInfo birthDate(Instant birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getCountry() {
        return country;
    }

    public UserInfo country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public UserInfo town(String town) {
        this.town = town;
        return this;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostCode() {
        return postCode;
    }

    public UserInfo postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Set<Space> getSpaces() {
        return spaces;
    }

    public UserInfo spaces(Set<Space> spaces) {
        this.spaces = spaces;
        return this;
    }

    public UserInfo addSpaces(Space space) {
        this.spaces.add(space);
        space.setUserInfo(this);
        return this;
    }

    public UserInfo removeSpaces(Space space) {
        this.spaces.remove(space);
        space.setUserInfo(null);
        return this;
    }

    public void setSpaces(Set<Space> spaces) {
        this.spaces = spaces;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserInfo)) {
            return false;
        }
        return id != null && id.equals(((UserInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + getId() +
            ", sex='" + isSex() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", country='" + getCountry() + "'" +
            ", town='" + getTown() + "'" +
            ", postCode='" + getPostCode() + "'" +
            "}";
    }
}
