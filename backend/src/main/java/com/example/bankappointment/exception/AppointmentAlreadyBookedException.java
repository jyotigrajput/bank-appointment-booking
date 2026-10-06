package com.example.bankappointment.exception;

public class EmployeeUnavailableException extends RuntimeException {
    public EmployeeUnavailableException(String message) {
        super(message);
    }
}
