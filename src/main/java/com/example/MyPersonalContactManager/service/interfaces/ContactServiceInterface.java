package com.example.MyPersonalContactManager.service.interfaces;

import java.util.List;

public interface ContactServiceInterface<T, U> {
    T getContactById(String id);

    List<T> getAllContacts();

    T createContact(T contactDTO, String userID);

    U updateContact(String id, U newContact);

    boolean deleteContactById(String id);
}
