package com.example.bankappointment.repository;

import com.example.bankappointment.entity.BankService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankServiceRepository extends JpaRepository<BankService, Long> {
}
