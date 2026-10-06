package com.example.bankappointment.repository;

import com.example.bankappointment.entity.Appointment;
import com.example.bankappointment.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.employee.id = :employeeId AND a.appointmentDate = :date AND a.status <> :status")
    List<Appointment> findByEmployeeIdAndAppointmentDateAndStatusNot(Long employeeId, LocalDate date, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.customer.email = :email OR a.customer.phone = :phone")
    List<Appointment> findByCustomerEmailOrPhone(String email, String phone);
}
