package com.example.bankappointment.controller;

import com.example.bankappointment.entity.Branch;
import com.example.bankappointment.service.BranchService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping("/branches")
    public List<Branch> getBranches() {
        return branchService.getAllBranches();
    }

    @GetMapping("/branches/{id}")
    public Branch getBranch(@PathVariable Long id) {
        return branchService.getBranchById(id);
    }
}
