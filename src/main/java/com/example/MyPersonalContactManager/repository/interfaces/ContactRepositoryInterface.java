package com.example.MyPersonalContactManager.repository.interfaces;

import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.ContactDTOBig;

import java.util.List;

public interface ContactRepositoryInterface<T, U> {

    Contact createContact(Contact contact, String userID);

    T getContactByContactId(String id);

    List<Contact> getAllContacts();

    ContactDTOBig updateContact(String id, ContactDTOBig newContact);

    boolean deleteContactById(String id);

    List<Contact> getContactByUserId(String userId);

}
