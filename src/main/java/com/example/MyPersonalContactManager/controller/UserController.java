//package com.example.MyPersonalContactManager.controller;
//
//import com.example.MyPersonalContactManager.exceptions.EasyUserPasswordException;
//import com.example.MyPersonalContactManager.exceptions.InvalidLoginPasswordException;
//import com.example.MyPersonalContactManager.exceptions.UserAlreadyExistsException;
//import com.example.MyPersonalContactManager.models.Error;
//import com.example.MyPersonalContactManager.models.UserModels.UserDTOLogin;
//import com.example.MyPersonalContactManager.models.UserModels.UserDTORegister;
//import com.example.MyPersonalContactManager.models.UserModels.UserDTOResponse;
//import com.example.MyPersonalContactManager.service.interfaces.UserServiceInterface;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/users")
//public class UserController {
//
//    public final UserServiceInterface dbUserService;
//
//
//    @PostMapping(value = "/userRegistration", consumes = "application/json")
//    public ResponseEntity<UserDTOResponse> userRegistration(@Valid @RequestBody UserDTORegister user) {
//        UserDTOResponse userResponse = dbUserService.registerUser(user);
//        return ResponseEntity.ok(userResponse);
//    }
//
//    @PostMapping("/userLogin")
//    public ResponseEntity<UserDTOResponse> userAuthorization(@Valid @RequestBody UserDTOLogin userDto) {
//        UserDTOResponse userResponse = dbUserService.loginUser(userDto);
//        return ResponseEntity.ok(userResponse);
//    }
//
//
//    //обработка исключений
//    @ExceptionHandler({EasyUserPasswordException.class, InvalidLoginPasswordException.class, UserAlreadyExistsException.class})
//    public ResponseEntity<UserDTOResponse> handlerCheckDataException(Exception exception) {
//        UserDTOResponse response = UserDTOResponse.builder()
//                .error(new Error(500, exception.getMessage()))
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<UserDTOResponse> handlerCheckLoginException(MethodArgumentNotValidException exception) {
//        UserDTOResponse response = UserDTOResponse.builder()
//                .error(new Error(500, "You login is too easy"))
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//}
