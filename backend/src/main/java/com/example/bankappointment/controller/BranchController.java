package com.example.bankappointment.service;

import com.example.bankappointment.dto.AvailabilitySlot;
import com.example.bankappointment.dto.CreateAppointmentRequest;
import com.example.bankappointment.entity.*;
import com.example.bankappointment.exception.*;
import com.example.bankappointment.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
        String e = email == null ? "" : email.trim();
        String p = phone == null ? "" : phone.trim();
        if (e.isEmpty() && p.isEmpty()) {
            return List.of();
        }
        return appointmentRepository.findByCustomerEmailOrPhone(e, p);
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
            throw new ServiceUnavailableException("Service is not active.");
        }

        LocalDate date = request.getAppointmentDate();
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidAppointmentDateException("Appointment date cannot be in the past.");
        }

        LocalTime start = request.getStartTime();
        LocalTime end = start.plusMinutes(bankService.getEstimatedDuration());

        if (start.isBefore(branch.getOpeningTime()) || end.isAfter(branch.getClosingTime())) {
            throw new BranchClosedException("Appointment is outside branch working hours.");
        }

        Employee selectedEmployee = findAvailableEmployee(branch.getId(), bankService.getId(), date, start, end);
        if (selectedEmployee == null) {
            throw new AppointmentAlreadyBookedException("The selected appointment slot is no longer available.");
        }

        Customer customer = customerRepository.findByEmail(request.getCustomerEmail())
                .or(() -> customerRepository.findByPhone(request.getCustomerPhone()))
                .orElseGet(() -> customerRepository.save(new Customer(
                        request.getCustomerName(),
                        request.getCustomerEmail(),
                        request.getCustomerPhone(),
                        generateCustomerReference())));

        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setBranch(branch);
        appointment.setService(bankService);
        appointment.setEmployee(selectedEmployee);
        appointment.setAppointmentDate(date);
        appointment.setStartTime(start);
        appointment.setEndTime(end);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setAppointmentReference(generateAppointmentReference(date));

        return appointmentRepository.save(appointment);
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
        LocalTime slotEnd;

        while (true) {
            slotEnd = current.plusMinutes(bankService.getEstimatedDuration());
            if (!slotEnd.isAfter(closing)) {
                boolean available = hasAvailableEmployee(branchId, serviceId, date, current, slotEnd);
                slots.add(new AvailabilitySlot(current, slotEnd, available));
                current = current.plusMinutes(30);
                if (current.equals(closing) || current.isAfter(closing)) {
                    break;
                }
            } else {
                break;
            }
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

    private Employee findAvailableEmployee(Long branchId, Long serviceId, LocalDate date, LocalTime start, LocalTime end) {
        List<Employee> employees = employeeRepository.findByBranchIdAndStatus(branchId, EmployeeStatus.ACTIVE);
        for (Employee employee : employees) {
            if (!employeeServiceRepository.existsByEmployeeIdAndBankServiceId(employee.getId(), serviceId)) {
                continue;
            }
            if (isEmployeeFree(employee, date, start, end)) {
                return employee;
            }
        }
        return null;
    }

    private boolean hasAvailableEmployee(Long branchId, Long serviceId, LocalDate date, LocalTime start, LocalTime end) {
        return findAvailableEmployee(branchId, serviceId, date, start, end) != null;
    }

    private boolean isEmployeeFree(Employee employee, LocalDate date, LocalTime start, LocalTime end) {
        List<Appointment> appointments = appointmentRepository.findByEmployeeIdAndAppointmentDateAndStatusNot(
                employee.getId(), date, AppointmentStatus.CANCELLED);

        for (Appointment appointment : appointments) {
            LocalTime existingStart = appointment.getStartTime();
            LocalTime existingEnd = appointment.getEndTime();
            boolean overlap = start.isBefore(existingEnd) && end.isAfter(existingStart);
            if (overlap) {
                return false;
            }
        }
        return true;
    }

    private void validateCustomer(CreateAppointmentRequest request) {
        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
            throw new InvalidCustomerDetailsException("Customer name is required.");
        }
        if (request.getCustomerEmail() == null || !request.getCustomerEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new InvalidCustomerDetailsException("Valid email is required.");
        }
        if (request.getCustomerPhone() == null || !request.getCustomerPhone().matches("^[0-9]{10,15}$")) {
            throw new InvalidCustomerDetailsException("Valid phone number is required.");
        }
    }

    private String generateAppointmentReference(LocalDate date) {
        long count = appointmentRepository.count() + 1;
        return String.format("APT-%s-%04d", date.toString().replace("-", ""), count);
    }

    private String generateCustomerReference() {
        long count = customerRepository.count() + 1;
        return String.format("CUST-%s-%04d", LocalDate.now().toString().replace("-", ""), count);
    }
}
