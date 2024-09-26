package com.example.MyPersonalContactManager.models.ContactModels;

import com.example.MyPersonalContactManager.utils.UtilsContact;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.MyPersonalContactManager.utils.ConstantsContact.DEFAULT_BIRTHDAY;

@Getter
@Setter
public class ContactDTOBig {
    @NotBlank
    private String firstName;

    private String lastName;

    private String email;

    private List<Phone> phones;

    private LocalDate birthday;

    private String address;

    private URL photo;

    private LocalDateTime lastUpdateDate;

    public ContactDTOBig(String firstName, String lastName, String email, List<Phone> phones, LocalDate birthday,
                         String address, URL photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phones = phones;
        this.birthday = birthday;
        this.address = address;
        this.photo = photo;
        this.lastUpdateDate = LocalDateTime.now();
    }

    public ContactDTOBig() {
        this.lastUpdateDate = LocalDateTime.now();
    }

    public LocalDate getBirthday() {
        UtilsContact utilsContact = new UtilsContact();
        if (utilsContact.isBirthdayDefault(birthday)) {
            return null;
        }
        return this.birthday;
    }

    public void setBirthday(LocalDate birthday) {
        UtilsContact utilsContact = new UtilsContact();

        if (utilsContact.isDateNull(birthday)) {
            this.birthday = DEFAULT_BIRTHDAY;
        } else this.birthday = birthday;
    }

    public void setPhones(List<Phone> phones) {

        this.phones = new ArrayList<>(phones);
    }

    @Override
    public String toString() {
        return "ContactDTOBig{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phones + '\'' +
                ", birthday=" + birthday +
                ", address='" + address + '\'' +
                ", photo=" + photo +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
