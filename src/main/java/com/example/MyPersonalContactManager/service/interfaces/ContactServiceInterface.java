package com.example.MyPersonalContactManager.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ContactServiceInterface<T, U> {
    T getContactById(String id);

    List<T> getAllContacts(HttpServletRequest request);

    T createContact(HttpServletRequest request, T contactDTO);

    U updateContact(String id, U newContact);

    boolean deleteContactById(String id);
}
