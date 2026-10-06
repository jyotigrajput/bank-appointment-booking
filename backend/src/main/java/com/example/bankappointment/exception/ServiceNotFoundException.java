package com.example.bankappointment.exception;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(Long id) {
        super("Branch not found with id: " + id);
    }
}
