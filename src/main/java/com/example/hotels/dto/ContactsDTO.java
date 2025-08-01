package com.example.hotels.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContactsDTO {
    @NotBlank(message = "Phone must not be blank")
    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    @Email(message = "Email should be valid")
    private String email;

    public ContactsDTO() {
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
}