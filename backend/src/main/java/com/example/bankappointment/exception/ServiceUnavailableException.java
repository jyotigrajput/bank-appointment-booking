package com.example.bankappointment.exception;

public class BranchClosedException extends RuntimeException {
    public BranchClosedException(String message) {
        super(message);
    }
}
