package com.example.bankappointment.controller;

import com.example.bankappointment.dto.AvailabilitySlot;
import com.example.bankappointment.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AvailabilityController {

    private final AppointmentService appointmentService;

    public AvailabilityController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/availability")
    public List<AvailabilitySlot> getAvailability(
            @RequestParam Long branchId,
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.getAvailabilitySlots(branchId, serviceId, date);
    }
}
