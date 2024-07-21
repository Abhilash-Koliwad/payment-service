package com.abhilash.payment_service.service;

import com.abhilash.payment_service.domain.Branch;
import com.abhilash.payment_service.repository.BranchConnectionRepository;
import com.abhilash.payment_service.repository.BranchRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
        return branchConnectionRepository.findByOriginBranchIdAndDestinationBranchId(originBranchId, destinationBranchId)
                .map(branchConnection -> String.join(",", originBranch.getName(), destinationBranch.getName()))
                .orElse(processPayment(originBranch, destinationBranch));
    }

    private String processPayment(Branch originBranch, Branch destinationBranch) {
        Map<Branch, BigDecimal> minCost = new HashMap<>();
        Map<Branch, Branch> previousBranch = new HashMap<>();
        PriorityQueue<Branch> queue = new PriorityQueue<>(Comparator.comparing(minCost::get));

        branchRepository.findAll().forEach(branch -> {
            BigDecimal costFromOriginBranch = branch.equals(originBranch) ? BigDecimal.ZERO : BigDecimal.valueOf(Long.MAX_VALUE);
            minCost.put(branch, costFromOriginBranch);
        });

        queue.add(originBranch);

        while (!queue.isEmpty()) {
            Branch current = queue.poll();
            if (current.equals(destinationBranch)) {
                return buildPath(previousBranch, destinationBranch);
            }
            branchConnectionRepository.findByOriginBranchId(current.getId()).forEach(connection -> {
                Branch neighbor = connection.getDestinationBranch();
                BigDecimal newCost = minCost.get(current).add(current.getTransferCost());
                if (newCost.compareTo(minCost.get(neighbor)) < 0) {
                    queue.remove(neighbor);
                    minCost.put(neighbor, newCost);
                    previousBranch.put(neighbor, current);
                    queue.add(neighbor);
                }
            });
        }
        return null;
    }

    private String buildPath(Map<Branch, Branch> previousBranch, Branch destinationBranch) {
        List<String> path = new LinkedList<>();
        for (Branch branch = destinationBranch; branch != null; branch = previousBranch.get(branch)) {
            path.addFirst(branch.getName());
        }
        return String.join(",", path);
    }

    private Branch findBranchById(UUID id) {
        return branchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Branch with id " + id + " not found"));
    }

}
