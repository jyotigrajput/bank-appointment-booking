package com.example.bankappointment.controller;

import com.example.bankappointment.entity.BankService;
import com.example.bankappointment.service.BankServiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ServiceController {

    private final BankServiceService bankServiceService;

    public ServiceController(BankServiceService bankServiceService) {
        this.bankServiceService = bankServiceService;
    }

    @GetMapping("/services")
    public List<BankService> getServices() {
        return bankServiceService.getAllServices();
    }

    @GetMapping("/services/{id}")
    public BankService getService(@PathVariable Long id) {
        return bankServiceService.getServiceById(id);
    }
}
