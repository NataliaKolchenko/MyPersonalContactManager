package com.example.MyPersonalContactManager.models.ContactModels;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContactDTOShort {
    @NotBlank
    private String firstName;
    @NotBlank
    private List<String> phones;

    public ContactDTOShort(String firstName, List<String> phones) {
        this.firstName = firstName;
        this.phones = phones;
    }
}
