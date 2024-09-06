package com.example.MyPersonalContactManager.repository;

import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.ContactDTOBig;
import com.example.MyPersonalContactManager.models.ContactModels.Phone;
import com.example.MyPersonalContactManager.repository.interfaces.ContactRepositoryInterface;
import com.example.MyPersonalContactManager.repository.interfaces.JpaContactRepositoryInterface;
import com.example.MyPersonalContactManager.repository.interfaces.JpaPhoneRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
@RequiredArgsConstructor
public class ContactRepositoryAdapter implements ContactRepositoryInterface<Contact, String> {

    private final JpaContactRepositoryInterface jpaContactRepository;
    private final JpaPhoneRepositoryInterface jpaPhoneRepository;


    @Override
    public Contact createContact(Contact contact, String userID) {
        contact.setOwnerId(userID);
        return jpaContactRepository.save(contact);
    }

    @Override
    public Contact getContactByContactId(String id) {
        return jpaContactRepository.findById(id).get();
    }

    @Override
    public List<Contact> getAllContacts() {
        return jpaContactRepository.findAll();
    }

    @Override
    public ContactDTOBig updateContact(String id, ContactDTOBig newContact) {
        Contact contact = toEntity(newContact);
        contact.setId(id);
        jpaContactRepository.save(contact);
        ContactDTOBig dto = toDTOBig(contact);
        return dto;
    }

    @Override
    public boolean deleteContactById(String id) {
        jpaContactRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Phone> getPhoneListByContactId(String contactId) {
        return jpaContactRepository.findPhoneListByContactId(contactId);
    }

    @Override
    public List<Contact> getContactByUserId(String userId) {
        return null;
    }

    @Override
    public List<Phone> createPhone(List<Phone> phones, String contactId) {
        for (Phone phone : phones) {
            phone.setContactId(contactId);
        }
        jpaPhoneRepository.saveAll(phones);
        return phones;
    }

    private Contact toEntity(ContactDTOBig contactDTO) {
        Contact contact = new Contact();
        contact.setFirstName(contactDTO.getFirstName());
        contact.setLastName(contactDTO.getLastName());
        contact.setEmail(contactDTO.getEmail());
        contact.setPhones(contactDTO.getPhones());
        contact.setBirthday(contactDTO.getBirthday());
        contact.setAddress(contactDTO.getAddress());
        contact.setPhoto(contactDTO.getPhoto());
        return contact;
    }

    private ContactDTOBig toDTOBig(Contact contact) {
        ContactDTOBig dto = new ContactDTOBig();
        dto.setFirstName(contact.getFirstName());
        dto.setLastName(contact.getLastName());
        dto.setEmail(contact.getEmail());
        dto.setPhones(contact.getPhones());
        dto.setBirthday(contact.getBirthday());
        dto.setAddress(contact.getAddress());
        dto.setPhoto(contact.getPhoto());
        return dto;
    }
}
