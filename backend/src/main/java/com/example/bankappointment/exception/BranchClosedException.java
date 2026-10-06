package com.example.bankappointment.exception;

public class InvalidAppointmentTimeException extends RuntimeException {
    public InvalidAppointmentTimeException(String message) {
        super(message);
    }
}
