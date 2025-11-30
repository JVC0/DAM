package org.docencia.hotel.model;

import org.docencia.hotel.model.nosql.GuestPreferences;
import org.springframework.data.annotation.Id;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "guest")
public class Guest {
    @Id
    private String id;
    @Column(name = "full_name", nullable = false)
    private String full_name;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Transient
    private GuestPreferences preferences;

    public Guest() {
    }

    public Guest(String id, String full_name, String email, String phone, GuestPreferences preferences) {
        this.id = id;
        this.full_name = full_name;
        this.email = email;
        this.phone = phone;
        this.preferences = preferences;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return this.full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public GuestPreferences getPreferences() {
        return this.preferences;
    }

    public void setPreferences(GuestPreferences preferences) {
        this.preferences = preferences;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Guest)) {
            return false;
        }
        Guest guest = (Guest) o;
        return Objects.equals(id, guest.id) && Objects.equals(full_name, guest.full_name) && Objects.equals(email, guest.email) && Objects.equals(phone, guest.phone) && Objects.equals(preferences, guest.preferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, full_name, email, phone, preferences);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", full_name='" + getFull_name() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", preferences='" + getPreferences() + "'" +
            "}";
    }
    
}
