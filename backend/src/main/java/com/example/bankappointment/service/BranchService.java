package com.example.bankappointment.service;

import com.example.bankappointment.entity.BankService;
import com.example.bankappointment.exception.ServiceNotFoundException;
import com.example.bankappointment.repository.BankServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceService {

    private final BankServiceRepository bankServiceRepository;

    public BankServiceService(BankServiceRepository bankServiceRepository) {
        this.bankServiceRepository = bankServiceRepository;
    }

    public List<BankService> getAllServices() {
        return bankServiceRepository.findAll();
    }

    public BankService getServiceById(Long id) {
        return bankServiceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));
    }
}
