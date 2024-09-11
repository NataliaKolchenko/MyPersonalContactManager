package com.example.MyPersonalContactManager.exceptions;

public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(String msg) {
        super(msg);
    }
}
