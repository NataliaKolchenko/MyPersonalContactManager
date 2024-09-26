package com.example.MyPersonalContactManager.service;

import com.example.MyPersonalContactManager.exceptions.AccessDeniedDeleteContactException;
import com.example.MyPersonalContactManager.exceptions.AccessDeniedUpdateContactException;
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
    public Contact getContactById(HttpServletRequest request, String contactId) {
        String token = authInterceptor.getTokenExtraction(request);
        tokenValidator.validateRequestToken(request);

        Contact tempContact = (Contact) contactRepository.getContactByContactId(contactId);
        return tempContact;
    }

    private List<Contact> getContactByUserId(String userId) {
        List<Contact> tempContact = contactRepository.getContactByUserId(userId);
        return tempContact;
    }

    @Override
    public List<Contact> getAllContacts(HttpServletRequest request) {
        List<Contact> tempListAllContacts;

        String token = authInterceptor.getTokenExtraction(request);
        tokenValidator.validateRequestToken(request);

        int userId = authInterceptor.extractUserIdFromToken(token);
        String userRole = authInterceptor.extractUserRoleFromToken(token);

        if (userRole.equals("ADMIN")) {
            tempListAllContacts = contactRepository.getAllContacts();
        } else {
            tempListAllContacts = contactRepository.getContactByUserId(String.valueOf(userId));
        }
        return tempListAllContacts;
    }

    @Override
    public Contact createContact(HttpServletRequest request, Contact clientContact) {
        String token = authInterceptor.getTokenExtraction(request);
        tokenValidator.validateRequestToken(request);

        int userId = authInterceptor.extractUserIdFromToken(token);

        Contact contact = contactRepository.createContact(clientContact, String.valueOf(userId));
        return contact;
    }

    @Override
    public ContactDTOBig updateContact(HttpServletRequest request, String contactId, ContactDTOBig updatedContact) {
        String token = authInterceptor.getTokenExtraction(request);
        tokenValidator.validateRequestToken(request);

        Contact contact = (Contact) contactRepository.getContactByContactId(contactId);
        int userId = authInterceptor.extractUserIdFromToken(token);
        String userIdString = String.valueOf(userId);
        String userRole = authInterceptor.extractUserRoleFromToken(token);

        if (userRole.equals("ADMIN")) {
            contactRepository.updateContact(userId, contactId, updatedContact);
        } else {
            if (contact.getOwnerId().equals(userIdString)) {
                contactRepository.updateContact(userId, contactId, updatedContact);
            } else {
                throw new AccessDeniedUpdateContactException("Access Denied Update Contact");
            }
        }


        return updatedContact;
    }

    @Override
    public boolean deleteContactById(HttpServletRequest request, String contactId) {
        String token = authInterceptor.getTokenExtraction(request);
        tokenValidator.validateRequestToken(request);

        int userId = authInterceptor.extractUserIdFromToken(token);
        String userRole = authInterceptor.extractUserRoleFromToken(token);
        Contact contact = (Contact) contactRepository.getContactByContactId(contactId);
        boolean isDeleted;

        if ((String.valueOf(userId)).equals(contact.getOwnerId()) || (userRole.equals("ADMIN"))) {
            isDeleted = contactRepository.deleteContactById(contactId);
        } else {

            throw new AccessDeniedDeleteContactException("Access Denied");
        }

        return isDeleted;
    }
}
