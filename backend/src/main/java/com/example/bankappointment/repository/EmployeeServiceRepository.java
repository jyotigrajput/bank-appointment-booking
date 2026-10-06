package com.example.bankappointment.repository;

import com.example.bankappointment.entity.Employee;
import com.example.bankappointment.entity.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByBranchIdAndStatus(Long branchId, EmployeeStatus status);
}
