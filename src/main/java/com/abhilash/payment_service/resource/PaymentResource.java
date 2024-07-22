package com.abhilash.payment_service.resource;

import com.abhilash.payment_service.resource.dto.ProcessPaymentRequestDto;
import com.abhilash.payment_service.resource.dto.ProcessPaymentResponseDto;
import com.abhilash.payment_service.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payment")
public class PaymentResource implements IPaymentResource {

    private final IPaymentService paymentService;

    @Autowired
    public PaymentResource(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    @PostMapping("process")
    public ResponseEntity<ProcessPaymentResponseDto> processPayment(@RequestBody ProcessPaymentRequestDto dto) {
        String branchSequence = paymentService.processPayment(dto.getOriginBranchId(), dto.getDestinationBranchId());
        return ResponseEntity.ok(new ProcessPaymentResponseDto(branchSequence));
    }

}
