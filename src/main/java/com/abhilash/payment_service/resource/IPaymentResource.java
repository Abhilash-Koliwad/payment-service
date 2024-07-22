package com.abhilash.payment_service.resource;

import com.abhilash.payment_service.resource.dto.ProcessPaymentRequestDto;
import com.abhilash.payment_service.resource.dto.ProcessPaymentResponseDto;
import org.springframework.http.ResponseEntity;

public interface IPaymentResource {

    ResponseEntity<ProcessPaymentResponseDto> processPayment(ProcessPaymentRequestDto dto);

}
