package com.example.MyPersonalContactManager.service;

import com.example.MyPersonalContactManager.exceptions.ValidateTokenException;
import com.example.MyPersonalContactManager.infrastructure.AuthInterceptor;
import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.ContactDTOBig;
import com.example.MyPersonalContactManager.repository.interfaces.ContactRepositoryInterface;
import com.example.MyPersonalContactManager.service.interfaces.ContactServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContactServiceImp implements ContactServiceInterface<Contact, ContactDTOBig> {

    @Autowired
    private ContactRepositoryInterface contactRepository;
    @Autowired
    private AuthInterceptor authInterceptor;
    @Autowired
    private TokenValidationService tokenValidator;

    @Override
    public Contact getContactById(String contactId) {
        Contact tempContact = (Contact) contactRepository.getContactByContactId(contactId);
        return tempContact;
    }

    public List<Contact> getContactByUserId(String userId) {
        List<Contact> tempContact = contactRepository.getContactByUserId(userId);
        return tempContact;
    }

    @Override
    public List<Contact> getAllContacts(HttpServletRequest request) {
        String token = authInterceptor.getTokenExtraction(request);
        boolean checkTokenValidation = tokenValidator.validateToken(token);
        if (!checkTokenValidation) {
            throw new ValidateTokenException("Unauthorized: Invalid or missing token");
        }

        int userId = authInterceptor.extractUserIdFromToken(token);
//        System.out.println("UserId: " + userId);
        List<Contact> tempListAllContacts = contactRepository.getAllContacts();
        return tempListAllContacts;
    }

    @Override
    public Contact createContact(Contact contact, String userID) {
        Contact tempContact = contactRepository.createContact(contact, userID);
        return tempContact;
    }

    @Override
    public ContactDTOBig updateContact(String id, ContactDTOBig newContact) {
        contactRepository.updateContact(id, newContact);
        return newContact;
    }

    @Override
    public boolean deleteContactById(String contactId) {
        contactRepository.deleteContactById(contactId);
        return true;
    }
}
