package com.example.MyPersonalContactManager.repository;

import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.ContactDTOBig;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Repository
public class DatabaseContactRepository implements ContactRepositoryInterface <Contact, ContactDTOBig>{

    private final JdbcTemplate jdbcTemplate;

    public DatabaseContactRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Contact> contactRowMapper = (ResultSet rs, int rowNum) -> {
        Contact contact = new Contact();
        contact.setId(rs.getString("id"));
        contact.setFirstName(rs.getString("First_Name"));
        contact.setLastName(rs.getString("Last_Name"));
        contact.setEmail(rs.getString("Email"));
        contact.setPhone(rs.getString("Phone"));
        contact.setBirthday(rs.getDate("Birth_Day").toLocalDate());
        contact.setAddress(rs.getString("Address"));
        contact.setPhoto(rs.getURL("Photo"));
        contact.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
        contact.setLastUpdateDate(rs.getTimestamp("Last_Update_Date").toLocalDateTime());
        return contact;
    };
    private final RowMapper<ContactDTOBig> contactDTOBigRowMapper = (ResultSet rs, int rowNum) -> {
        ContactDTOBig contactDTO = new ContactDTOBig();
        contactDTO.setFirstName(rs.getString("First_Name"));
        contactDTO.setLastName(rs.getString("Last_Name"));
        contactDTO.setEmail(rs.getString("Email"));
        contactDTO.setPhone(rs.getString("Phone"));
        contactDTO.setBirthday(rs.getDate("Birth_Day").toLocalDate());
        contactDTO.setAddress(rs.getString("Address"));
        contactDTO.setPhoto(rs.getURL("Photo"));
        contactDTO.setLastUpdateDate(rs.getTimestamp("Last_Update_Date").toLocalDateTime());
        return contactDTO;
    };

    @Override
    public Contact createContact(Contact contact) {
        String sql = "INSERT INTO Contacts (id, First_Name, Last_Name, Email, Phone, Birth_Day, " +
                "Address, Photo, Create_Date, Last_Update_Date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, contact.getId());
            ps.setString(2, contact.getFirstName());
            ps.setString(3, contact.getLastName());
            ps.setString(4, contact.getEmail());
            ps.setString(5, contact.getPhone());
            ps.setString(6, String.valueOf(contact.getBirthday()));
            ps.setString(7, contact.getAddress());
            ps.setString(8, String.valueOf(contact.getPhoto()));
            ps.setString(9, String.valueOf(contact.getCreateDate()));
            ps.setString(10, String.valueOf(contact.getLastUpdateDate()));

            return ps;
        }, keyHolder);

        String selectSql = "SELECT * FROM Contacts WHERE id = ?";
        return jdbcTemplate.queryForObject(selectSql, contactRowMapper, contact.getId());

    }

    @Override
    public Contact getContactById(String id) {
        String selectSql = "SELECT * FROM Contacts WHERE id = ?";
        return jdbcTemplate.queryForObject(selectSql, contactRowMapper, id);
    }

    @Override
    public List<Contact> getAllContacts() {
        String selectSql = "SELECT * FROM Contacts";
        List<Contact> contactList = jdbcTemplate.query(selectSql, contactRowMapper);


        return contactList.stream().toList();
    }

    @Override
    public ContactDTOBig updateContact(String id, ContactDTOBig newContact) {
        String sql = "update Contacts " +
                "set First_Name = ? , Last_Name = ? , Email = ?, Phone = ?, Birth_Day = ?,Address = ?,  " +
                "Photo = ?, Last_Update_Date = ? where id = ?";

//        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,Statement.NO_GENERATED_KEYS);
            ps.setString(1, newContact.getFirstName());
            ps.setString(2, newContact.getLastName());
            ps.setString(3, newContact.getEmail());
            ps.setString(4, newContact.getPhone());
            ps.setString(5, String.valueOf(newContact.getBirthday()));
            ps.setString(6, newContact.getAddress());
            ps.setString(7, String.valueOf(newContact.getPhoto()));
            ps.setString(8, String.valueOf(newContact.getLastUpdateDate()));
            ps.setString(9, id);
            return ps;});

        String selectSql = "SELECT * FROM Contacts WHERE id = ?";
        return jdbcTemplate.queryForObject(selectSql, contactDTOBigRowMapper, id);
    }

    @Override
    public boolean deleteContactById(String id) {
        return false;
    }
}
