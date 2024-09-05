package com.example.MyPersonalContactManager.repository.interfaces;

import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.ContactDTOBig;
import com.example.MyPersonalContactManager.models.ContactModels.Phone;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface ContactRepositoryInterface<T, U> {

    Contact createContact(Contact contact, String userID);

    T getContactByContactId(String id);

    List<Contact> getAllContacts();

    ContactDTOBig updateContact(String id, ContactDTOBig newContact);

    boolean deleteContactById(String id);

    List<Phone> getPhoneListByContactId(String contactId);

    List<Contact> getContactByUserId(String userId);

    List<Phone> createPhone(@NotEmpty List<Phone> phones, String id);
}
