package com.example.MyPersonalContactManager.controller;

import com.example.MyPersonalContactManager.exceptions.AccessDeniedDeleteContactException;
import com.example.MyPersonalContactManager.exceptions.ValidateTokenException;
import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.ContactDTOBig;
import com.example.MyPersonalContactManager.models.Error;
import com.example.MyPersonalContactManager.models.RequestResponseModels.RequestBodyClient;
import com.example.MyPersonalContactManager.models.RequestResponseModels.ResponseAPI;
import com.example.MyPersonalContactManager.service.ContactServiceImp;
import com.example.MyPersonalContactManager.service.UserServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/MyPersonalContactManager")
public class ContactController {

    private ResponseAPI responseAPI;

    @Autowired
    private ContactServiceImp dbContactServiceImp;
    @Autowired
    private UserServiceImp dbUserService;

    @PostMapping(value = "/createContact", consumes = "application/json")
    public ResponseEntity<ResponseAPI> crateContact(HttpServletRequest request,
                                                    @Valid @RequestBody RequestBodyClient requestBodyClient) {
        Contact contact = dbContactServiceImp.createContact(request, requestBodyClient.contact);
        responseAPI = new ResponseAPI();
        responseAPI.response = contact;
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/contacts/{contactId}")
    public ResponseEntity<ResponseAPI> getContactById(HttpServletRequest request,
                                                      @PathVariable String contactId) {
        responseAPI = new ResponseAPI();
        Contact contact = dbContactServiceImp.getContactById(request, contactId);
        responseAPI.response = contact;
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping(value = "/contacts")
    public ResponseEntity<ResponseAPI> getAllContacts(HttpServletRequest request) {
        responseAPI = new ResponseAPI();
        try {
            List<Contact> allContacts = dbContactServiceImp.getAllContacts(request);
            responseAPI.response = allContacts;
            return ResponseEntity.ok(responseAPI);
        } catch (ValidateTokenException e) {
            responseAPI.response = new Error(401, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseAPI);
        }
    }

    @PutMapping(value = "/updateContact/{contactId}", consumes = "application/json")
    public ResponseEntity<ResponseAPI> updateContact(HttpServletRequest request,
                                                     @PathVariable String contactId,
                                                     @RequestBody RequestBodyClient requestBodyClient) {
        responseAPI = new ResponseAPI();
        try {
            ContactDTOBig contact = dbContactServiceImp.updateContact(request, contactId, requestBodyClient.contactDTOBig);
            responseAPI.response = contact;
            return ResponseEntity.ok(responseAPI);
        } catch (AccessDeniedDeleteContactException e) {
            responseAPI.response = new Error(403, e.getMessage());
            return ResponseEntity.ok(responseAPI);
        }
    }

    @DeleteMapping("contacts/delete")
    public ResponseEntity<ResponseAPI> deleteContactById(HttpServletRequest request,
                                                         @RequestHeader("Contact-Id") String contactId) {
        responseAPI = new ResponseAPI();
        try {
            boolean isDeleted = dbContactServiceImp.deleteContactById(request, contactId);
            responseAPI.response = isDeleted;
            return ResponseEntity.ok(responseAPI);
        } catch (AccessDeniedDeleteContactException e) {
            responseAPI.response = new Error(403, e.getMessage());
            return ResponseEntity.ok(responseAPI);
        }
    }
}