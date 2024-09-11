package com.example.MyPersonalContactManager.repository.interfaces;

import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaContactRepositoryInterface extends JpaRepository<Contact, String> {

}
