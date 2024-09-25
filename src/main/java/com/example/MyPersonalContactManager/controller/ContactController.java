package com.example.MyPersonalContactManager.controller;

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
//        if (token == null || token.isEmpty()) {
//            responseAPI.response = new Error(400, "Authorization header is missing.");
//            return ResponseEntity.badRequest().body(responseAPI);
//        }
//        boolean userRole = dbUserService.getUserRoleByToken(token);
//        String userId = dbUserService.getUserIdByToken(token);
        Contact contact = dbContactServiceImp.getContactById(request, contactId);
        responseAPI.response = contact;
//        if (userId.equals(contact.getOwnerId()) || userRole) {
//            responseAPI.response = contact;
//        } else {
//            responseAPI.response = new Error(403, "Access denied.");
//        }


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

    @PutMapping(value = "/updateContact/{id}", consumes = "application/json")
    public ResponseEntity<ResponseAPI> updateContact(@PathVariable String id,
                                                     @RequestHeader("token") String token,
                                                     @RequestBody RequestBodyClient requestBodyClient) {
        if (token == null || token.isEmpty()) {
            responseAPI.response = new Error(400, "Authorization header is missing.");
            return ResponseEntity.badRequest().body(responseAPI);
        }

        boolean userRole = dbUserService.getUserRoleByToken(token);
        responseAPI = new ResponseAPI();
        if (userRole) {
            ContactDTOBig contactDTOBig = dbContactServiceImp.updateContact(id, requestBodyClient.contactDTOBig);
            responseAPI.response = contactDTOBig;
            return ResponseEntity.ok(responseAPI);
        } else {
            responseAPI.response = new Error(403, "Access denied.");
            return ResponseEntity.ok(responseAPI);
        }
    }

//    @DeleteMapping("contacts/delete")
//
//    public ResponseEntity<ResponseAPI> deleteContactById(@RequestHeader("Contact-Id") String contactId,
//                                                         @RequestHeader("token") String token) {
//        responseAPI = new ResponseAPI();
//        if (token == null || token.isEmpty()) {
//            responseAPI.response = new Error(400, "Authorization header is missing.");
//            return ResponseEntity.badRequest().body(responseAPI);
//        }
//
//        boolean userRole = dbUserService.getUserRoleByToken(token);
//        String userId = dbUserService.getUserIdByToken(token);
////        Contact contact = dbContactServiceImp.getContactById(contactId);
//        boolean isDeleted;
//        if (userId.equals(contact.getOwnerId()) || userRole) {
//            isDeleted = dbContactServiceImp.deleteContactById(contactId);
//            responseAPI.response = isDeleted;
//        } else {
//            responseAPI.response = new Error(403, "Access denied.");
//        }
//
//        return ResponseEntity.ok(responseAPI);
//    }
}