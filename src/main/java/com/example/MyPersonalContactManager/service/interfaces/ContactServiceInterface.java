package com.example.MyPersonalContactManager.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ContactServiceInterface<T, U> {
    T getContactById(HttpServletRequest request, String id);

    List<T> getAllContacts(HttpServletRequest request);

    T createContact(HttpServletRequest request, T contactDTO);

    U updateContact(HttpServletRequest request, String contactId, U newContact);

    boolean deleteContactById(HttpServletRequest request, String id);
}
