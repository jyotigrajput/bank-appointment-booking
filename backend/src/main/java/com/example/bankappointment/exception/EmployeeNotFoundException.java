package com.example.bankappointment.exception;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(Long id) {
        super("Service not found with id: " + id);
    }
}
