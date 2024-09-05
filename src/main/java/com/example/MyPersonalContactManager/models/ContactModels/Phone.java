package com.example.MyPersonalContactManager.models.ContactModels;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Contacts_Phones")
public class Phone {
    @Id
    private String id;
    @Column(name = "Phone_Number")
    private String phoneNumber;
    @Column(name = "Create_Date")
    private LocalDateTime createDate;
    @Column(name = "Last_Update_Date")
    private LocalDateTime lastUpdateDate;

    public Phone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.createDate = LocalDateTime.now();
        this.lastUpdateDate = LocalDateTime.now();
    }

    public Phone() {
        this.createDate = LocalDateTime.now();
        this.lastUpdateDate = LocalDateTime.now();
    }
}
