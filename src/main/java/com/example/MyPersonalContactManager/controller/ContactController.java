package com.example.MyPersonalContactManager.controller;

import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.ContactDTOBig;
import com.example.MyPersonalContactManager.models.RequestResponseModels.RequestBodyClient;
import com.example.MyPersonalContactManager.models.RequestResponseModels.ResponseAPI;
import com.example.MyPersonalContactManager.service.DatabaseContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/MyPersonalContactManager")
public class ContactController {

    private ResponseAPI responseAPI;

    @Autowired
    private DatabaseContactService dbService;


    @PostMapping(value = "/createContact", consumes = "application/json")
    public ResponseEntity<ResponseAPI> crateContact(@Valid @RequestBody RequestBodyClient requestBodyClient) {
        responseAPI = new ResponseAPI();

        Contact contact = dbService.createContact(requestBodyClient.contact);
        responseAPI.response = contact;
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/contacts/{id}")
    public ResponseEntity<ResponseAPI> getContactById(@PathVariable String id) {
        responseAPI = new ResponseAPI();
        Contact contact = dbService.getContactById(id);
        responseAPI.response = contact;
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping(value = "/contacts")
    public ResponseEntity<ResponseAPI> getAllContacts() {
        responseAPI = new ResponseAPI();
        responseAPI.response = dbService.getAllContacts().toArray();
        return ResponseEntity.ok(responseAPI);
    }

    @PutMapping(value = "/updateContact")
    public ResponseEntity<ResponseAPI> updateContact(@PathVariable String id, @RequestBody RequestBodyClient requestBodyClient) {
        responseAPI = new ResponseAPI();
        ContactDTOBig contactDTOBig = dbService.updateContact(id, requestBodyClient.contactDTOBig);
        responseAPI.response = contactDTOBig;
        return ResponseEntity.ok(responseAPI);
    }

    @DeleteMapping("contacts/{id}")
    public ResponseEntity<Boolean> deleteContactById(@PathVariable String id) {
        boolean isDeleted = dbService.deleteContactById(id);
        return ResponseEntity.ok(isDeleted);
    }
}