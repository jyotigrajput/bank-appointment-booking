package com.example.bankappointment.exception;

public class AppointmentAlreadyBookedException extends RuntimeException {
    public AppointmentAlreadyBookedException(String message) {
        super(message);
    }
}
