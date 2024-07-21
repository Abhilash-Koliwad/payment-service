package com.abhilash.payment_service.service;

import com.abhilash.payment_service.domain.Branch;
import com.abhilash.payment_service.repository.BranchConnectionRepository;
import com.abhilash.payment_service.repository.BranchRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService implements IPaymentService {

    private final BranchRepository branchRepository;
    private final BranchConnectionRepository branchConnectionRepository;

    @Autowired
    public PaymentService(BranchRepository branchRepository, BranchConnectionRepository branchConnectionRepository) {
        this.branchRepository = branchRepository;
        this.branchConnectionRepository = branchConnectionRepository;
    }

    @Override
    public String processPayment(UUID originBranchId, UUID destinationBranchId) {
        Branch originBranch = findBranchById(originBranchId);
        Branch destinationBranch = findBranchById(destinationBranchId);

        return "Abhilash";
    }

    private Branch findBranchById(UUID id) {
        return branchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Branch with id " + id + " not found"));
    }

}
