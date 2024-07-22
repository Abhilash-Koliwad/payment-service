package com.abhilash.payment_service.service;

import com.abhilash.payment_service.domain.Branch;
import com.abhilash.payment_service.domain.BranchConnection;
import com.abhilash.payment_service.repository.BranchConnectionRepository;
import com.abhilash.payment_service.repository.BranchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class PaymentServiceTest {

    private PaymentService paymentService;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private BranchConnectionRepository branchConnectionRepository;

    private Branch branchA;
    private Branch branchB;
    private Branch branchC;
    private Branch branchD;
    private BranchConnection branchAToB;
    private BranchConnection branchBToD;
    private BranchConnection branchCToD;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentService(branchRepository, branchConnectionRepository);
        branchA = buildBranch("A", new BigDecimal("5"));
        branchB = buildBranch("B", new BigDecimal("50"));
        branchC = buildBranch("C", new BigDecimal("10"));
        branchD = buildBranch("D", new BigDecimal("10"));
        branchAToB = buildBranchConnection(branchA, branchB);
        branchBToD = buildBranchConnection(branchB, branchD);
        branchCToD = buildBranchConnection(branchC, branchD);
        Mockito.when(branchRepository.findAll()).thenReturn(Arrays.asList(branchA, branchB, branchC, branchD));
        Mockito.when(branchRepository.findById(branchA.getId())).thenReturn(Optional.of(branchA));
        Mockito.when(branchRepository.findById(branchB.getId())).thenReturn(Optional.of(branchB));
        Mockito.when(branchRepository.findById(branchC.getId())).thenReturn(Optional.of(branchC));
        Mockito.when(branchRepository.findById(branchD.getId())).thenReturn(Optional.of(branchD));
        Mockito.when(branchConnectionRepository.findByOriginBranchId(branchA.getId())).thenReturn(Collections.singletonList(branchAToB));
        Mockito.when(branchConnectionRepository.findByOriginBranchId(branchB.getId())).thenReturn(Collections.singletonList(branchBToD));
        Mockito.when(branchConnectionRepository.findByOriginBranchId(branchC.getId())).thenReturn(Collections.singletonList(branchCToD));
        Mockito.when(branchConnectionRepository.findByOriginBranchId(branchD.getId())).thenReturn(Collections.emptyList());
    }

    @Test
    public void processPayment_directBranchConnection_returnsValidBranchSequence() {
        // Given
        UUID branchAId = branchA.getId();
        UUID branchBId = branchB.getId();
        Mockito.when(branchConnectionRepository.findByOriginBranchIdAndDestinationBranchId(branchAId, branchBId)).thenReturn(Optional.of(branchAToB));

        // When
        String response = paymentService.processPayment(branchAId, branchBId);

        // Then
        Assertions.assertEquals("A,B", response);
    }

    @Test
    public void processPayment_indirectBranchConnection_returnsValidBranchSequence() {
        // Given
        UUID branchAId = branchA.getId();
        UUID branchDId = branchD.getId();
        Mockito.when(branchConnectionRepository.findByOriginBranchIdAndDestinationBranchId(branchAId, branchDId)).thenReturn(Optional.empty());


        // When
        String response = paymentService.processPayment(branchAId, branchDId);

        // Then
        Assertions.assertEquals("A,B,D", response);
    }

    @Test
    public void processPayment_noBranchConnection_returnsNull() {
        // Given
        UUID branchAId = branchA.getId();
        UUID branchCId = branchC.getId();
        Mockito.when(branchConnectionRepository.findByOriginBranchIdAndDestinationBranchId(branchAId, branchCId)).thenReturn(Optional.empty());

        // When
        String response = paymentService.processPayment(branchAId, branchCId);

        // Then
        Assertions.assertNull(response);
    }

    private Branch buildBranch(String name, BigDecimal transferCost) {
        return new Branch(UUID.randomUUID(), name, transferCost, LocalDateTime.now(), LocalDateTime.now(), 0L);
    }

    private BranchConnection buildBranchConnection(Branch originBranch, Branch destinationBranch) {
        return new BranchConnection(UUID.randomUUID(), originBranch, destinationBranch, LocalDateTime.now(), LocalDateTime.now(), 0L);
    }

}
