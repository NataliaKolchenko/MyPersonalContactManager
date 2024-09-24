package com.example.MyPersonalContactManager.controller;

import com.example.MyPersonalContactManager.models.ContactModels.Contact;
import com.example.MyPersonalContactManager.models.ContactModels.ContactDTOBig;
import com.example.MyPersonalContactManager.models.Error;
import com.example.MyPersonalContactManager.models.RequestResponseModels.RequestBodyClient;
import com.example.MyPersonalContactManager.models.RequestResponseModels.ResponseAPI;
import com.example.MyPersonalContactManager.models.UserModels.User;
import com.example.MyPersonalContactManager.service.ContactServiceImp;
import com.example.MyPersonalContactManager.service.UserServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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
    public ResponseEntity<ResponseAPI> crateContact(@Valid @RequestBody RequestBodyClient requestBodyClient,
                                                    @RequestHeader("token") String token) {
        if (token == null || token.isEmpty()) {
            responseAPI.response = new Error(400, "Authorization header is missing.");
            return ResponseEntity.badRequest().body(responseAPI);
        }
        String userId = dbUserService.getUserIdByToken(token);
        Contact contact = dbContactServiceImp.createContact(requestBodyClient.contact, userId);
        responseAPI = new ResponseAPI();
        responseAPI.response = contact;
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/contacts/{contactId}")
    public ResponseEntity<ResponseAPI> getContactById(@RequestHeader("token") String token,
                                                      @PathVariable String contactId) {
        responseAPI = new ResponseAPI();
        if (token == null || token.isEmpty()) {
            responseAPI.response = new Error(400, "Authorization header is missing.");
            return ResponseEntity.badRequest().body(responseAPI);
        }
        boolean userRole = dbUserService.getUserRoleByToken(token);
        String userId = dbUserService.getUserIdByToken(token);
        Contact contact = dbContactServiceImp.getContactById(contactId);
        if (userId.equals(contact.getOwnerId()) || userRole) {
            responseAPI.response = contact;
        } else {
            responseAPI.response = new Error(403, "Access denied.");
        }


        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping(value = "/contacts")
    public ResponseEntity<ResponseAPI> getAllContacts(HttpServletRequest request) {
        responseAPI = new ResponseAPI();
        // Извлекаем информацию о текущем пользователе // Получаем текущую аутентификацию
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Извлечение ролей (userRoles)
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        List<String> userRoles = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        System.out.println(userRoles);

        // Извлекаем информацию о пользователе
        Object principal = authentication.getPrincipal();
        String userId = null;
        if (principal instanceof UserDetails) {
            // Пример получения userId, если он хранится в UserDetails
            userId = ((User) principal).getUserId();
        } else {
            // Если principal — это просто имя пользователя (например, если не используется UserDetails)
            userId = principal.toString();
        }
        System.out.println(userId);

        List<Contact> allContacts = dbContactServiceImp.getAllContacts();

//        boolean userRole = dbUserService.getUserRoleByToken(token);
//        String userId = dbUserService.getUserIdByToken(token);

//        if (userId.isEmpty()) {
//            responseAPI.response = new Error(403, "Access denied.");
//        } else if (userRole) {
//            List<Contact> allContacts = dbContactServiceImp.getAllContacts();
//            responseAPI.response = allContacts;
//        } else {
//            List<Contact> contactListByUserId = dbContactServiceImp.getContactByUserId(userId);
//            responseAPI.response = contactListByUserId;
//        }

        return ResponseEntity.ok(responseAPI);
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

    @DeleteMapping("contacts/delete")

    public ResponseEntity<ResponseAPI> deleteContactById(@RequestHeader("Contact-Id") String contactId,
                                                         @RequestHeader("token") String token) {
        responseAPI = new ResponseAPI();
        if (token == null || token.isEmpty()) {
            responseAPI.response = new Error(400, "Authorization header is missing.");
            return ResponseEntity.badRequest().body(responseAPI);
        }

        boolean userRole = dbUserService.getUserRoleByToken(token);
        String userId = dbUserService.getUserIdByToken(token);
        Contact contact = dbContactServiceImp.getContactById(contactId);
        boolean isDeleted;
        if (userId.equals(contact.getOwnerId()) || userRole) {
            isDeleted = dbContactServiceImp.deleteContactById(contactId);
            responseAPI.response = isDeleted;
        } else {
            responseAPI.response = new Error(403, "Access denied.");
        }

        return ResponseEntity.ok(responseAPI);
    }
}