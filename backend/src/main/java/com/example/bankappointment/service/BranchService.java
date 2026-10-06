package com.example.bankappointment.exception;

import com.example.bankappointment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BranchNotFoundException.class)
    public ErrorResponse handleBranchNotFound(BranchNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "BRANCH_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public ErrorResponse handleServiceNotFound(ServiceNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "SERVICE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ErrorResponse handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "EMPLOYEE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ErrorResponse handleAppointmentNotFound(AppointmentNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "APPOINTMENT_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(InvalidAppointmentDateException.class)
    public ErrorResponse handleInvalidAppointmentDate(InvalidAppointmentDateException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "INVALID_APPOINTMENT_DATE", ex.getMessage());
    }

    @ExceptionHandler(InvalidAppointmentTimeException.class)
    public ErrorResponse handleInvalidAppointmentTime(InvalidAppointmentTimeException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "INVALID_APPOINTMENT_TIME", ex.getMessage());
    }

    @ExceptionHandler(BranchClosedException.class)
    public ErrorResponse handleBranchClosed(BranchClosedException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "BRANCH_CLOSED", ex.getMessage());
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ErrorResponse handleServiceUnavailable(ServiceUnavailableException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "SERVICE_UNAVAILABLE", ex.getMessage());
    }

    @ExceptionHandler(EmployeeUnavailableException.class)
    public ErrorResponse handleEmployeeUnavailable(EmployeeUnavailableException ex) {
        return buildError(HttpStatus.CONFLICT, "EMPLOYEE_UNAVAILABLE", ex.getMessage());
    }

    @ExceptionHandler(AppointmentAlreadyBookedException.class)
    public ErrorResponse handleAppointmentAlreadyBooked(AppointmentAlreadyBookedException ex) {
        return buildError(HttpStatus.CONFLICT, "APPOINTMENT_ALREADY_BOOKED", ex.getMessage());
    }

    @ExceptionHandler(InvalidCustomerDetailsException.class)
    public ErrorResponse handleInvalidCustomerDetails(InvalidCustomerDetailsException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "INVALID_CUSTOMER_DETAILS", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed.";
        return buildError(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneric(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "An unexpected error occurred.");
    }

    private ErrorResponse buildError(HttpStatus status, String code, String message) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), code, message);
    }
}
