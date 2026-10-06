package com.example.bankappointment.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmployeeServiceId implements Serializable {
    private Long employeeId;
    private Long serviceId;

    public EmployeeServiceId() {}

    public EmployeeServiceId(Long employeeId, Long serviceId) {
        this.employeeId = employeeId;
        this.serviceId = serviceId;
    }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeServiceId that)) return false;
        return Objects.equals(employeeId, that.employeeId) && Objects.equals(serviceId, that.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, serviceId);
    }
}
