package com.example.hotels.model.mongo;

import java.util.Objects;

public class ContactsMongo {

    private String phone;
    private String email;

    public ContactsMongo() {
    }

    public ContactsMongo(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactsMongo)) return false;
        ContactsMongo that = (ContactsMongo) o;
        return Objects.equals(phone, that.phone) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, email);
    }

    @Override
    public String toString() {
        return "ContactsMongo{" +
                "phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
