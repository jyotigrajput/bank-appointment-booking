package com.example.bankappointment.dto;

import java.time.LocalTime;

public record AvailabilitySlot(LocalTime startTime, LocalTime endTime, boolean available) {
}
