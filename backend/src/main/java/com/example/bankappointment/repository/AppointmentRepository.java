package com.example.bankappointment.repository;

import com.example.bankappointment.entity.EmployeeService;
import com.example.bankappointment.entity.EmployeeServiceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeServiceRepository extends JpaRepository<EmployeeService, EmployeeServiceId> {
    boolean existsByEmployeeIdAndBankServiceId(Long employeeId, Long serviceId);
}
