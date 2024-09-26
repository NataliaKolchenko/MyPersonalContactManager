package com.example.MyPersonalContactManager.repository;

import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.ContactDTOBig;
import com.example.MyPersonalContactManager.models.ContactModels.Phone;
import com.example.MyPersonalContactManager.repository.interfaces.ContactRepositoryInterface;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

//@Primary
@Repository
public class JdbcContactRepositoryImp implements ContactRepositoryInterface<Contact, ContactDTOBig> {

    private final JdbcTemplate jdbcTemplate;

    public JdbcContactRepositoryImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Contact> contactRowMapper = (ResultSet rs, int rowNum) -> {
        Contact contact = new Contact();
        contact.setId(rs.getString("id"));
        contact.setFirstName(rs.getString("First_Name"));
        contact.setLastName(rs.getString("Last_Name"));
        contact.setEmail(rs.getString("Email"));
        contact.setBirthday(rs.getDate("Birth_Day").toLocalDate());
        contact.setAddress(rs.getString("Address"));
        contact.setPhoto(rs.getURL("Photo"));
        contact.setOwnerId(rs.getString("Owner_Id"));
        contact.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
        contact.setLastUpdateDate(rs.getTimestamp("Last_Update_Date").toLocalDateTime());
        return contact;
    };
    private final RowMapper<ContactDTOBig> contactDTOBigRowMapper = (ResultSet rs, int rowNum) -> {
        ContactDTOBig contactDTO = new ContactDTOBig();
        contactDTO.setFirstName(rs.getString("First_Name"));
        contactDTO.setLastName(rs.getString("Last_Name"));
        contactDTO.setEmail(rs.getString("Email"));
        contactDTO.setBirthday(rs.getDate("Birth_Day").toLocalDate());
        contactDTO.setAddress(rs.getString("Address"));
        contactDTO.setPhoto(rs.getURL("Photo"));
        contactDTO.setLastUpdateDate(rs.getTimestamp("Last_Update_Date").toLocalDateTime());
        return contactDTO;
    };

    private final RowMapper<Phone> phoneRowMapper = (rs, rowNum) -> {
        Phone phone = new Phone();
        phone.setId(Long.valueOf(rs.getString("id")));
        phone.setPhoneNumber(rs.getString("Phone_Number"));
        phone.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
        phone.setLastUpdateDate(rs.getTimestamp("Last_Update_Date").toLocalDateTime());
        return phone;
    };

    @Override
    public Contact createContact(Contact contact, String userID) {
        String sqlContacts = "INSERT INTO Contacts (id, First_Name, Last_Name, Email, Birth_Day, " +
                "Address, Photo, Owner_Id, Create_Date, Last_Update_Date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolderForContacts = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlContacts, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, contact.getId());
            ps.setString(2, contact.getFirstName());
            ps.setString(3, contact.getLastName());
            ps.setString(4, contact.getEmail());
            ps.setString(5, String.valueOf(contact.getBirthday()));
            ps.setString(6, contact.getAddress());
            ps.setString(7, String.valueOf(contact.getPhoto()));
            ps.setString(8, String.valueOf(userID));
            ps.setString(9, String.valueOf((contact.getCreateDate())));
            ps.setString(10, String.valueOf(contact.getLastUpdateDate()));

            return ps;
        }, keyHolderForContacts);

        String selectSql = "SELECT * FROM Contacts WHERE id = ?";
        return jdbcTemplate.queryForObject(selectSql, contactRowMapper, contact.getId());

    }

    public List<Phone> createPhone(List<Phone> phoneList, String contactId) {
        String sqlPhoneNumbers = "INSERT INTO Contacts_Phones (Contact_Id, Phone_Number, Create_Date, Last_Update_Date)" +
                "VALUES (?, ?, ?, ?)";


        for (Phone phone : phoneList) {
            KeyHolder keyHolderForPhoneNumbers = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlPhoneNumbers, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, contactId);
                ps.setString(2, phone.getPhoneNumber());
                ps.setString(3, String.valueOf(phone.getCreateDate().toLocalDate()));
                ps.setString(4, String.valueOf(phone.getLastUpdateDate().toLocalDate()));
                return ps;
            }, keyHolderForPhoneNumbers);
        }
        String selectSql = "SELECT * FROM Contacts_Phones WHERE Contact_Id = ?";
        return jdbcTemplate.query(selectSql, phoneRowMapper, contactId);
    }

    @Override
    public Contact getContactByContactId(String contactId) {
        String selectSql = "SELECT * FROM Contacts WHERE id = ?";
        return jdbcTemplate.queryForObject(selectSql, contactRowMapper, contactId);
    }

    public List<Contact> getContactByUserId(String userId) {
        String selectSql = "SELECT * FROM Contacts WHERE Owner_Id = ?";
        return jdbcTemplate.query(selectSql, contactRowMapper, userId);
    }

    public List<Phone> getPhoneListByContactId(String contactId) {
        String selectSql = "SELECT * FROM Contacts_Phones WHERE Contact_Id = ?";
        return jdbcTemplate.query(selectSql, phoneRowMapper, contactId);
    }

    @Override
    public List<Contact> getAllContacts() {
        String selectSql = "SELECT * FROM Contacts";
        List<Contact> contactList = jdbcTemplate.query(selectSql, contactRowMapper);


        return contactList.stream().toList();
    }


    @Override
    public ContactDTOBig updateContact(int UserId, String id, ContactDTOBig newContact) {
        String sql = "UPDATE Contacts " +
                "set First_Name = ? , Last_Name = ? , Email = ?, Phone = ?, Birth_Day = ?,Address = ?,  " +
                "Photo = ?, Last_Update_Date = ? where id = ?";

//        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
            ps.setString(1, newContact.getFirstName());
            ps.setString(2, newContact.getLastName());
            ps.setString(3, newContact.getEmail());
            ps.setString(5, String.valueOf(newContact.getBirthday()));
            ps.setString(6, newContact.getAddress());
            ps.setString(7, String.valueOf(newContact.getPhoto()));
            ps.setString(8, String.valueOf(newContact.getLastUpdateDate()));
            ps.setString(9, id);
            return ps;
        });

        String selectSql = "SELECT * FROM Contacts WHERE id = ?";
        return jdbcTemplate.queryForObject(selectSql, contactDTOBigRowMapper, id);
    }

    @Override
    public boolean deleteContactById(String id) {
        return jdbcTemplate.update("DELETE FROM Contacts WHERE id = ?", id) > 0;
    }
}
