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

    //        boolean userRole = dbUserService.getUserRoleByToken(token);
//        String userId = dbUserService.getUserIdByToken(token);

    //        if (userId.isEmpty()) {
//            responseAPI.response = new Error(403, "Access denied.");
//        } else if (userRole) {
//
//            responseAPI.response = allContacts;
//        } else {
//
//            responseAPI.response = contactListByUserId;
//        }
    @Override
    public List<Contact> getAllContacts(HttpServletRequest request) {
        List tempListAllContacts;
        String token = authInterceptor.getTokenExtraction(request);
        boolean checkTokenValidation = tokenValidator.validateToken(token);
        if (!checkTokenValidation) {
            throw new ValidateTokenException("Unauthorized: Invalid or missing token");
        }

        int userId = authInterceptor.extractUserIdFromToken(token);
        String userRole = authInterceptor.extractUserRoleFromToken(token);
        System.out.println(userRole);
        if (userRole.equals("ADMIN")) {
            tempListAllContacts = contactRepository.getAllContacts();
        } else {
            tempListAllContacts = contactRepository.getContactByUserId(String.valueOf(userId));
        }
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
