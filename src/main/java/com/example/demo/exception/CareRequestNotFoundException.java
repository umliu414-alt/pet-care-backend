package com.example.demo.exception;

public class CareRequestNotFoundException extends RuntimeException {

    public CareRequestNotFoundException(String message) {
        super(message);
    }
}
