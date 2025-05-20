package com.markdev.inventoryManagmentsSystem.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message){
        super(message);
    }
}
