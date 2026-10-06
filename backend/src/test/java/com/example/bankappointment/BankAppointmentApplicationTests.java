package com.example.bankappointment;

import com.example.bankappointment.dto.CreateAppointmentRequest;
import com.example.bankappointment.entity.*;
import com.example.bankappointment.exception.*;
import com.example.bankappointment.repository.*;
import com.example.bankappointment.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AppointmentServiceTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BankServiceRepository bankServiceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceRepository employeeServiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        Branch branch = new Branch();
        branch.setBranchCode("BR-100");
        branch.setBranchName("Main Branch");
        branch.setAddress("123 Main Street");
        branch.setCity("Pune");
        branch.setState("Maharashtra");
        branch.setPincode("411001");
        branch.setPhone("9876543210");
        branch.setEmail("branch@test.com");
        branch.setOpeningTime(LocalTime.of(9, 0));
        branch.setClosingTime(LocalTime.of(17, 0));
        branch.setStatus(BranchStatus.ACTIVE);
        branchRepository.save(branch);

        BankService service = new BankService();
        service.setName("Loan Enquiry");
        service.setDescription("Loan information");
        service.setEstimatedDuration(30);
        service.setStatus(ServiceStatus.ACTIVE);
        service = bankServiceRepository.save(service);

        Employee employee = new Employee();
        employee.setEmployeeCode("EMP-001");
        employee.setName("John Smith");
        employee.setDesignation("Loan Officer");
        employee.setEmail("john@test.com");
        employee.setPhone("9988776655");
        employee.setBranch(branch);
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee = employeeRepository.save(employee);

        EmployeeService mapping = new EmployeeService();
        mapping.setId(new EmployeeServiceId(employee.getId(), service.getId()));
        mapping.setEmployee(employee);
        mapping.setBankService(service);
        employeeServiceRepository.save(mapping);
    }

    @Test
    void createAppointmentSuccessfully() {
        CreateAppointmentRequest request = new CreateAppointmentRequest();
        request.setCustomerName("Alice Johnson");
        request.setCustomerEmail("alice@example.com");
        request.setCustomerPhone("9876543211");
        request.setBranchId(1L);
        request.setServiceId(1L);
        request.setAppointmentDate(LocalDate.now().plusDays(1));
        request.setStartTime(LocalTime.of(10, 0));

        Appointment appointment = appointmentService.createAppointment(request);
        assertNotNull(appointment);
        assertEquals(AppointmentStatus.CONFIRMED, appointment.getStatus());
        assertNotNull(appointment.getAppointmentReference());
    }

    @Test
    void rejectPastAppointment() {
        CreateAppointmentRequest request = new CreateAppointmentRequest();
        request.setCustomerName("Past Customer");
        request.setCustomerEmail("past@example.com");
        request.setCustomerPhone("7654321098");
        request.setBranchId(1L);
        request.setServiceId(1L);
        request.setAppointmentDate(LocalDate.now().minusDays(1));
        request.setStartTime(LocalTime.of(11, 0));

        assertThrows(InvalidAppointmentDateException.class, () -> appointmentService.createAppointment(request));
    }

    @Test
    void preventDoubleBooking() {
        CreateAppointmentRequest first = new CreateAppointmentRequest();
        first.setCustomerName("A");
        first.setCustomerEmail("a@example.com");
        first.setCustomerPhone("1111111111");
        first.setBranchId(1L);
        first.setServiceId(1L);
        first.setAppointmentDate(LocalDate.now().plusDays(2));
        first.setStartTime(LocalTime.of(10, 0));
        appointmentService.createAppointment(first);

        CreateAppointmentRequest second = new CreateAppointmentRequest();
        second.setCustomerName("B");
        second.setCustomerEmail("b@example.com");
        second.setCustomerPhone("2222222222");
        second.setBranchId(1L);
        second.setServiceId(1L);
        second.setAppointmentDate(LocalDate.now().plusDays(2));
        second.setStartTime(LocalTime.of(10, 0));

        assertThrows(AppointmentAlreadyBookedException.class, () -> appointmentService.createAppointment(second));
    }

    @Test
    void cancelAppointment() {
        CreateAppointmentRequest request = new CreateAppointmentRequest();
        request.setCustomerName("Cancel User");
        request.setCustomerEmail("cancel@example.com");
        request.setCustomerPhone("3333333333");
        request.setBranchId(1L);
        request.setServiceId(1L);
        request.setAppointmentDate(LocalDate.now().plusDays(3));
        request.setStartTime(LocalTime.of(11, 0));

        Appointment created = appointmentService.createAppointment(request);
        Appointment cancelled = appointmentService.cancelAppointment(created.getId());
        assertEquals(AppointmentStatus.CANCELLED, cancelled.getStatus());
    }

    @Test
    void rejectInactiveBranch() {
        Branch branch = branchRepository.findById(1L).orElseThrow();
        branch.setStatus(BranchStatus.INACTIVE);
        branchRepository.save(branch);

        CreateAppointmentRequest request = new CreateAppointmentRequest();
        request.setCustomerName("Inactive Branch User");
        request.setCustomerEmail("inactivebranch@example.com");
        request.setCustomerPhone("4444444444");
        request.setBranchId(1L);
        request.setServiceId(1L);
        request.setAppointmentDate(LocalDate.now().plusDays(4));
        request.setStartTime(LocalTime.of(9, 30));

        assertThrows(ServiceUnavailableException.class, () -> appointmentService.createAppointment(request));
    }

    @Test
    void rejectInactiveService() {
        BankService service = bankServiceRepository.findById(1L).orElseThrow();
        service.setStatus(ServiceStatus.INACTIVE);
        bankServiceRepository.save(service);

        CreateAppointmentRequest request = new CreateAppointmentRequest();
        request.setCustomerName("Inactive Service User");
        request.setCustomerEmail("inactiveservice@example.com");
        request.setCustomerPhone("5555555555");
        request.setBranchId(1L);
        request.setServiceId(1L);
        request.setAppointmentDate(LocalDate.now().plusDays(4));
        request.setStartTime(LocalTime.of(9, 30));

        assertThrows(ServiceUnavailableException.class, () -> appointmentService.createAppointment(request));
    }

    @Test
    void validateCustomerInformation() {
        CreateAppointmentRequest request = new CreateAppointmentRequest();
        request.setCustomerName(" ");
        request.setCustomerEmail("bad-email");
        request.setCustomerPhone("abc");
        request.setBranchId(1L);
        request.setServiceId(1L);
        request.setAppointmentDate(LocalDate.now().plusDays(1));
        request.setStartTime(LocalTime.of(10, 30));

        assertThrows(InvalidCustomerDetailsException.class, () -> appointmentService.createAppointment(request));
    }
}
