package com.example.bankappointment.exception;

public class InvalidCustomerDetailsException extends RuntimeException {
    public InvalidCustomerDetailsException(String message) {
        super(message);
    }
}
