package com.example.MyPersonalContactManager.service;

import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.ContactDTOBig;
import com.example.MyPersonalContactManager.models.ContactModels.Phone;
import com.example.MyPersonalContactManager.repository.interfaces.ContactRepositoryInterface;
import com.example.MyPersonalContactManager.service.interfaces.ContactServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContactServiceImp implements ContactServiceInterface<Contact, ContactDTOBig> {
//    public ContactServiceImp(JdbcContactRepositoryImp contactRepository) {
//        this.contactRepository = contactRepository;
//    }

    @Autowired
    private ContactRepositoryInterface contactRepository;

    @Override
    public Contact getContactById(String contactId) {
        Contact tempContact = (Contact) contactRepository.getContactByContactId(contactId);
        List<Phone> phoneList = contactRepository.getPhoneListByContactId(contactId);
        tempContact.setPhones(phoneList);
        return tempContact;
    }

    public List<Contact> getContactByUserId(String userId) {
        List<Contact> tempContact = contactRepository.getContactByUserId(userId);
        for (int i = 0; i < tempContact.size(); i++) {
            List<Phone> phoneList = contactRepository.getPhoneListByContactId(tempContact.get(i).getId());
            tempContact.get(i).setPhones(phoneList);
        }
        return tempContact;
    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> tempListAllContacts = contactRepository.getAllContacts();
        for (int i = 0; i < tempListAllContacts.size(); i++) {
            List phoneList = contactRepository.getPhoneListByContactId(tempListAllContacts.get(i).getId());
            tempListAllContacts.get(i).setPhones(phoneList);
        }
        return tempListAllContacts;
    }

    @Override
    public Contact createContact(Contact contact, String userID) {
        Contact tempContact = contactRepository.createContact(contact, userID);
        List<Phone> phoneList = contactRepository.createPhone(contact.getPhones(), tempContact.getId());
        tempContact.setPhones(phoneList);
//        tempContact = contactRepository.createContact(contact, userID);
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
