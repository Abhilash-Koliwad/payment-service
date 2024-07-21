package com.abhilash.payment_service.resource;

import com.abhilash.payment_service.resource.dto.ProcessPaymentResponseDto;
import com.abhilash.payment_service.service.IPaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.UUID;

public class PaymentResourceTest {

    private PaymentResource paymentResource;

    @Mock
    private IPaymentService paymentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentResource = new PaymentResource(paymentService);
    }

    @Test
    public void processPayment() {
        // Given
        UUID originBranchId = UUID.randomUUID();
        UUID destinationBranchId = UUID.randomUUID();
        String branchSequence = "A,D,C";
        Mockito.when(paymentService.processPayment(originBranchId, destinationBranchId)).thenReturn(branchSequence);

        // When
        ResponseEntity<ProcessPaymentResponseDto> response = paymentResource.processPayment(originBranchId, destinationBranchId);

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(branchSequence, Objects.requireNonNull(response.getBody()).getBranchSequence());
    }

}
