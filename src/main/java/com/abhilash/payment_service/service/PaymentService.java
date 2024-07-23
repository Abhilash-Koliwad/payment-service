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
                .orElseGet(() -> getIndirectBranchConnectionSequence(originBranch, destinationBranch));
    }

    private String getIndirectBranchConnectionSequence(Branch originBranch, Branch destinationBranch) {
        Map<Branch, BigDecimal> minCostFromOriginMap = initializeMinCostMap(originBranch);
        Map<Branch, Branch> previousBranchMap = new HashMap<>();
        PriorityQueue<Branch> priorityQueue = new PriorityQueue<>(Comparator.comparing(minCostFromOriginMap::get));
        priorityQueue.add(originBranch);
        while (!priorityQueue.isEmpty()) {
            Branch currentBranch = priorityQueue.poll();
            if (currentBranch.equals(destinationBranch)) {
                return buildBranchSequence(previousBranchMap, destinationBranch);
            }
            updateCostsForNeighborBranches(currentBranch, minCostFromOriginMap, previousBranchMap, priorityQueue);
        }
        return null;
    }

    private Map<Branch, BigDecimal> initializeMinCostMap(Branch originBranch) {
        Map<Branch, BigDecimal> minCostFromOriginMap = new HashMap<>();
        branchRepository.findAll().forEach(branch ->
                minCostFromOriginMap.put(branch, branch.equals(originBranch) ? BigDecimal.ZERO : BigDecimal.valueOf(Long.MAX_VALUE))
        );
        return minCostFromOriginMap;
    }

    private String buildBranchSequence(Map<Branch, Branch> previousBranch, Branch destinationBranch) {
        List<String> branchSequence = new LinkedList<>();
        for (Branch branch = destinationBranch; branch != null; branch = previousBranch.get(branch)) {
            branchSequence.addFirst(branch.getName());
        }
        return String.join(",", branchSequence);
    }

    private void updateCostsForNeighborBranches(
            Branch currentBranch,
            Map<Branch, BigDecimal> minCostFromOriginMap,
            Map<Branch, Branch> previousBranchMap,
            PriorityQueue<Branch> priorityQueue
    ) {
        branchConnectionRepository.findByOriginBranchId(currentBranch.getId()).forEach(connection -> {
            Branch neighborBranch = connection.getDestinationBranch();
            BigDecimal newCost = minCostFromOriginMap.get(currentBranch).add(currentBranch.getTransferCost());
            if (newCost.compareTo(minCostFromOriginMap.get(neighborBranch)) < 0) {
                priorityQueue.remove(neighborBranch);
                minCostFromOriginMap.put(neighborBranch, newCost);
                previousBranchMap.put(neighborBranch, currentBranch);
                priorityQueue.add(neighborBranch);
            }
        });
    }

    private Branch findBranchById(UUID id) {
        return branchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Branch with id " + id + " not found"));
    }

}
