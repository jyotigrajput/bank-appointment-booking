package com.example.bankappointment.service;

import com.example.bankappointment.entity.Branch;
import com.example.bankappointment.exception.BranchNotFoundException;
import com.example.bankappointment.repository.BranchRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BranchService {
    private final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Branch getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));
    }
}
