package com.example.bankappointment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_services")
public class EmployeeService {
    @EmbeddedId
    private EmployeeServiceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("serviceId")
    @JoinColumn(name = "service_id")
    private BankService bankService;

    public EmployeeService() {}

    public EmployeeServiceId getId() { return id; }
    public void setId(EmployeeServiceId id) { this.id = id; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public BankService getBankService() { return bankService; }
    public void setBankService(BankService bankService) { this.bankService = bankService; }
}
