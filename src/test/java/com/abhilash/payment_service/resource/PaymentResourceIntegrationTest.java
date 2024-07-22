package com.abhilash.payment_service.resource;

import com.abhilash.payment_service.domain.Branch;
import com.abhilash.payment_service.domain.BranchConnection;
import com.abhilash.payment_service.repository.BranchConnectionRepository;
import com.abhilash.payment_service.repository.BranchRepository;
import com.abhilash.payment_service.resource.dto.ProcessPaymentRequestDto;
import com.abhilash.payment_service.resource.dto.ProcessPaymentResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentResourceIntegrationTest {

    @Autowired
    private IPaymentResource paymentResource;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchConnectionRepository branchConnectionRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Branch branchA;
    private Branch branchB;
    private Branch branchC;
    private Branch branchD;
    private Branch branchE;
    private Branch branchF;

    @BeforeEach
    public void setUp() {
        branchA = createBranch("A", new BigDecimal("5"));
        branchB = createBranch("B", new BigDecimal("50"));
        branchC = createBranch("C", new BigDecimal("10"));
        branchD = createBranch("D", new BigDecimal("10"));
        branchE = createBranch("E", new BigDecimal("20"));
        branchF = createBranch("F", new BigDecimal("5"));
        createBranchConnection(branchA, branchB);
        createBranchConnection(branchA, branchC);
        createBranchConnection(branchC, branchB);
        createBranchConnection(branchB, branchD);
        createBranchConnection(branchC, branchE);
        createBranchConnection(branchD, branchE);
        createBranchConnection(branchE, branchD);
        createBranchConnection(branchD, branchF);
        createBranchConnection(branchE, branchF);
    }

    @AfterEach
    public void cleanUp() {
        jdbcTemplate.execute("DELETE FROM Branch_Connection");
        jdbcTemplate.execute("DELETE FROM Branch");
    }

    @Test
    public void processPayment_branchAToB_directBranchConnection_returnsValidBranchSequence() {
        // Given
        UUID branchAId = branchA.getId();
        UUID branchBId = branchB.getId();
        ProcessPaymentRequestDto dto = new ProcessPaymentRequestDto(branchAId, branchBId);

        // When
        ResponseEntity<ProcessPaymentResponseDto> response = paymentResource.processPayment(dto);

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("A,B", response.getBody().getBranchSequence());
    }

    @Test
    public void processPayment_branchAToC_directBranchConnection_returnsValidBranchSequence() {
        // Given
        UUID branchAId = branchA.getId();
        UUID branchCId = branchC.getId();
        ProcessPaymentRequestDto dto = new ProcessPaymentRequestDto(branchAId, branchCId);

        // When
        ResponseEntity<ProcessPaymentResponseDto> response = paymentResource.processPayment(dto);

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("A,C", response.getBody().getBranchSequence());
    }

    @Test
    public void processPayment_branchAToD_indirectBranchConnection_returnsValidBranchSequence() {
        // Given
        UUID branchAId = branchA.getId();
        UUID branchDId = branchD.getId();
        ProcessPaymentRequestDto dto = new ProcessPaymentRequestDto(branchAId, branchDId);

        // When
        ResponseEntity<ProcessPaymentResponseDto> response = paymentResource.processPayment(dto);

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("A,C,E,D", response.getBody().getBranchSequence());
    }


    @Test
    public void processPayment_branchAToF_indirectBranchConnection_returnsValidBranchSequence() {
        // Given
        UUID branchAId = branchA.getId();
        UUID branchFId = branchF.getId();
        ProcessPaymentRequestDto dto = new ProcessPaymentRequestDto(branchAId, branchFId);

        // When
        ResponseEntity<ProcessPaymentResponseDto> response = paymentResource.processPayment(dto);

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("A,C,E,F", response.getBody().getBranchSequence());
    }

    @Test
    public void processPayment_branchAToE_indirectBranchConnection_returnsValidBranchSequence() {
        // Given
        UUID branchAId = branchA.getId();
        UUID branchEId = branchE.getId();
        ProcessPaymentRequestDto dto = new ProcessPaymentRequestDto(branchAId, branchEId);

        // When
        ResponseEntity<ProcessPaymentResponseDto> response = paymentResource.processPayment(dto);

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("A,C,E", response.getBody().getBranchSequence());
    }


    @Test
    public void processPayment_noBranchConnection_returnsNull() {
        // Given
        UUID branchEId = branchE.getId();
        UUID branchAId = branchA.getId();
        ProcessPaymentRequestDto dto = new ProcessPaymentRequestDto(branchEId, branchAId);

        // When
        ResponseEntity<ProcessPaymentResponseDto> response = paymentResource.processPayment(dto);

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNull(response.getBody().getBranchSequence());
    }

    private Branch createBranch(String name, BigDecimal transferCost) {
        Branch branch = new Branch();
        branch.setName(name);
        branch.setTransferCost(transferCost);
        return branchRepository.save(branch);
    }

    private void createBranchConnection(Branch originBranch, Branch destinationBranch) {
        BranchConnection branchConnection = new BranchConnection();
        branchConnection.setOriginBranch(originBranch);
        branchConnection.setDestinationBranch(destinationBranch);
        branchConnectionRepository.save(branchConnection);
    }

}
