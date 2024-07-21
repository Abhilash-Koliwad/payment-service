package com.abhilash.payment_service.resource;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IPaymentResource {

    ResponseEntity<String> processPayment(UUID originBranchId, UUID destinationBranchId);

}
