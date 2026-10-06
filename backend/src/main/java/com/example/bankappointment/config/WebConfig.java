package com.example.bankappointment.service;

import com.example.bankappointment.dto.CreateAppointmentRequest;
import com.example.bankappointment.dto.AvailabilitySlot;
import com.example.bankappointment.entity.*;
import com.example.bankappointment.exception.*;
import com.example.bankappointment.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BranchRepository branchRepository;
    private final BankServiceRepository bankServiceRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeServiceRepository employeeServiceRepository;
    private final CustomerRepository customerRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              BranchRepository branchRepository,
                              BankServiceRepository bankServiceRepository,
                              EmployeeRepository employeeRepository,
                              EmployeeServiceRepository employeeServiceRepository,
                              CustomerRepository customerRepository) {
        this.appointmentRepository = appointmentRepository;
        this.branchRepository = branchRepository;
        this.bankServiceRepository = bankServiceRepository;
        this.employeeRepository = employeeRepository;
        this.employeeServiceRepository = employeeServiceRepository;
        this.customerRepository = customerRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
    }

    public List<Appointment> searchAppointmentsByCustomer(String email, String phone) {
        String normalizedEmail = email != null ? email.trim() : "";
        String normalizedPhone = phone != null ? phone.trim() : "";

        if (normalizedEmail.isEmpty() && normalizedPhone.isEmpty()) {
            return List.of();
        }

        return appointmentRepository.findByCustomerEmailOrPhone(normalizedEmail, normalizedPhone);
    }

    @Transactional
    public Appointment createAppointment(CreateAppointmentRequest request) {
        validateCustomer(request);

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException(request.getBranchId()));
        if (branch.getStatus() != BranchStatus.ACTIVE) {
            throw new ServiceUnavailableException("Branch is not active.");
        }

        BankService bankService = bankServiceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ServiceNotFoundException(request.getServiceId()));
        if (bankService.getStatus() != ServiceStatus.ACTIVE) {
            throw new ServiceUnavailableException("Selected service is not active.");
        }

        if (request.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new InvalidAppointmentDateException("Appointment date cannot be in the past.");
        }

        LocalTime start = request.getStartTime();
        LocalTime end = start.plusMinutes(bankService.getEstimatedDuration());
        if (end.isAfter(branch.getClosingTime()) || start.isBefore(branch.getOpeningTime())) {
            throw new BranchClosedException("Appointment time is outside the branch working hours.");
        }

        List<Employee> employees = employeeRepository.findByBranchIdAndStatus(branch.getId(), EmployeeStatus.ACTIVE);
        List<Employee> eligibleEmployees = employees.stream()
                .filter(employee -> employeeServiceRepository.existsByEmployeeIdAndBankServiceId(employee.getId(), bankService.getId()))
                .toList();

        if (eligibleEmployees.isEmpty()) {
            throw new EmployeeUnavailableException("No active employee is available for this service in the selected branch.");
        }

        Employee selectedEmployee = null;
        for (Employee employee : eligibleEmployees) {
            if (isTimeSlotAvailable(employee, request.getAppointmentDate(), request.getStartTime(), end)) {
                selectedEmployee = employee;
                break;
            }
        }

        if (selectedEmployee == null) {
            throw new AppointmentAlreadyBookedException("The selected appointment slot is no longer available.");
        }

        Customer customer = customerRepository.findByEmail(request.getCustomerEmail())
                .or(() -> customerRepository.findByPhone(request.getCustomerPhone()))
                .orElseGet(() -> customerRepository.save(new Customer(
                        request.getCustomerName(),
                        request.getCustomerEmail(),
                        request.getCustomerPhone(),
                        generateCustomerReference()
                )));

        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setBranch(branch);
        appointment.setService(bankService);
        appointment.setEmployee(selectedEmployee);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStartTime(start);
        appointment.setEndTime(end);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setAppointmentReference(generateAppointmentReference(request.getAppointmentDate()));

        return appointmentRepository.save(appointment);
                .tap(() -> {...});
    }

    public List<AvailabilitySlot> getAvailabilitySlots(Long branchId, Long serviceId, LocalDate date) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException(branchId));

        BankService bankService = bankServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));

        if (branch.getStatus() != BranchStatus.ACTIVE) {
            throw new ServiceUnavailableException("Branch is not active.");
        }
        if (bankService.getStatus() != ServiceStatus.ACTIVE) {
            throw new ServiceUnavailableException("Service is not active.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidAppointmentDateException("The selected date cannot be in the past.");
        }

        List<AvailabilitySlot> slots = new ArrayList<>();
        LocalTime current = branch.getOpeningTime();
        LocalTime closing = branch.getClosingTime();

        while (!current.plusMinutes(bankService.getEstimatedDuration()).isAfter(closing)) {
            LocalTime slotEnd = current.plusMinutes(bankService.getEstimatedDuration());
            boolean available = isAnyEmployeeAvailable(branchId, serviceId, date, current, slotEnd);
            slots.add(new AvailabilitySlot(current, slotEnd, available));
            current = current.plusMinutes(30);
        }

        return slots;
    }

    @Transactional
    public Appointment cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    private void validateCustomer(CreateAppointmentRequest request) {
        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
            throw new InvalidCustomerDetailsException("Customer name is required.");
        }
        if (request.getCustomerEmail() == null || !request.getCustomerEmail().matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            throw new InvalidCustomerDetailsException("Valid email is required.");
        }
        if (request.getCustomerPhone() == null || !request.getCustomerPhone().matches("^[0-9]{10,15}$")) {
            throw new InvalidCustomerDetailsException("Valid phone number is required.");
        }
    }

    private boolean isAnyEmployeeAvailable(Long branchId, Long serviceId, LocalDate date, LocalTime start, LocalTime end) {
        List<Employee> activeEmployees = employeeRepository.findByBranchIdAndStatus(branchId, EmployeeStatus.ACTIVE);
        for (Employee employee : activeEmployees) {
            if (!employeeServiceRepository.existsByEmployeeIdAndBankServiceId(employee.getId(), serviceId)) {
                continue;
            }
            if (isTimeSlotAvailable(employee, date, start, end)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTimeSlotAvailable(Employee employee, LocalDate date, LocalTime slotStart, LocalTime slotEnd) {
        List<Appointment> appointments = appointmentRepository.findByEmployeeIdAndAppointmentDateAndStatusNot(
                employee.getId(), date, AppointmentStatus.CANCELLED);

        for (Appointment appointment : appointments) {
            LocalTime existingStart = appointment.getStartTime();
            LocalTime existingEnd = appointment.getEndTime();
            boolean overlap = slotStart.isBefore(existingEnd) && slotEnd.isAfter(existingStart);
            if (overlap) {
                return false;
            }
        }

        return true;
    }

    private String generateAppointmentReference(LocalDate date) {
        long count = appointmentRepository.count() + 1;
        return String.format("APT-%s-%04d", date.toString().replace("-", ""), count);
    }

    private String generateCustomerReference() {
        long count = customerRepository.count() + 1;
        return String.format("CUST-%s-%04d", LocalDate.now(), count);
    }
}
