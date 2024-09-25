package com.example.MyPersonalContactManager.repository;

import com.example.MyPersonalContactManager.exceptions.ContactNotFoundException;
import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.ContactDTOBig;
import com.example.MyPersonalContactManager.models.ContactModels.Phone;
import com.example.MyPersonalContactManager.repository.interfaces.ContactRepositoryInterface;
import com.example.MyPersonalContactManager.repository.interfaces.JpaContactRepositoryInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
@RequiredArgsConstructor
public class ContactRepositoryAdapter implements ContactRepositoryInterface<Contact, String> {

    private final JpaContactRepositoryInterface jpaContactRepository;
    @PersistenceContext
    private EntityManager entityManager;


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
    public boolean deleteContactById(String contactId) {
        Contact contact = jpaContactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException("Incorrect contact id"));
        jpaContactRepository.delete(contact);

        return true;
    }


    @Override
    public List<Contact> getContactByUserId(String userId) {
        return entityManager.createQuery(
                        "FROM Contact e WHERE e.ownerId = :userId",
                        Contact.class)
                .setParameter("userId", userId)
                .getResultList();
    }


    public void createPhone(Phone phone, String contactId) {
        Contact contact = jpaContactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException("Incorrect contact id"));
        List<Phone> phones = contact.getPhones();
        phones.add(phone);
        jpaContactRepository.save(contact);
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
