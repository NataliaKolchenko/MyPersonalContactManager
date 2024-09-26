package com.example.MyPersonalContactManager.models.ContactModels;

import com.example.MyPersonalContactManager.utils.UtilsContact;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.MyPersonalContactManager.utils.ConstantsContact.DEFAULT_BIRTHDAY;

@Getter
@Setter
@Entity
@Table(name = "Contacts")
public class Contact {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "First_Name")
    @NotBlank
    private String firstName;

    @Column(name = "Last_Name")
    private String lastName;

    @Column(name = "Email")
    private String email;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "contactId")
    private List<Phone> phones;

    @Column(name = "Birth_Day")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column(name = "Address")
    private String address;

    @Column(name = "Photo")
    private URL photo;

    @Column(name = "Owner_Id")
    private String ownerId;

    @Column(name = "Create_Date")
    private LocalDateTime createDate;

    @Column(name = "Last_Update_Date")
    @Setter
    private LocalDateTime lastUpdateDate;

    public Contact() {
        this.id = String.valueOf(UUID.randomUUID());
        this.createDate = LocalDateTime.now();
        this.lastUpdateDate = LocalDateTime.now();
    }

    public Contact(String firstName, String lastName, String email, List<Phone> phones, LocalDate birthday,
                   String address, URL photo, String ownerId) {
        this.id = String.valueOf(UUID.randomUUID());
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phones = phones;
        this.birthday = birthday;
        this.address = address;
        this.photo = photo;
        this.ownerId = ownerId;
        this.createDate = LocalDateTime.now();
        this.lastUpdateDate = LocalDateTime.now();
    }

    public LocalDate getBirthday(LocalDate birthday) {
        UtilsContact utilsContact = new UtilsContact();
        if (utilsContact.isBirthdayDefault(this.birthday)) {
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
        return "Contact{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phones=" + phones +
                ", birthday=" + birthday +
                ", address='" + address + '\'' +
                ", photo=" + photo +
                ", ownerId='" + ownerId + '\'' +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
