package com.abhilash.payment_service.resource;

import com.abhilash.payment_service.resource.dto.ProcessPaymentResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IPaymentResource {

    ResponseEntity<ProcessPaymentResponseDto> processPayment(UUID originBranchId, UUID destinationBranchId);

}
