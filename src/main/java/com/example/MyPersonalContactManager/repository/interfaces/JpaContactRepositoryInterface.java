package com.example.MyPersonalContactManager.repository.interfaces;

import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.Phone;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaContactRepositoryInterface extends JpaRepository<Contact, String> {
    @Transactional
    @Query("select cp from Phone cp where cp.contactId = ?1")
    List<Phone> findPhoneListByContactId(String contactId);

}
