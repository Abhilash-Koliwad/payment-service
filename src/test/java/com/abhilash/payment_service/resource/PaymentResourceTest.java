package com.abhilash.payment_service.resource;

import com.abhilash.payment_service.service.IPaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

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
        String processPaymentResponse = "A,D,C";
        Mockito.when(paymentService.processPayment(originBranchId, destinationBranchId)).thenReturn(processPaymentResponse);

        // When
        ResponseEntity<String> response = paymentResource.processPayment(originBranchId, destinationBranchId);

        // Then
        Assertions.assertEquals(processPaymentResponse, response.getBody());
    }

}
