package com.abhilash.payment_service.resource;

import com.abhilash.payment_service.resource.dto.ProcessPaymentResponseDto;
import com.abhilash.payment_service.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("payment")
public class PaymentResource implements IPaymentResource {

    private final IPaymentService paymentService;

    @Autowired
    public PaymentResource(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    @GetMapping
    public ResponseEntity<ProcessPaymentResponseDto> processPayment(@RequestParam UUID originBranchId, @RequestParam UUID destinationBranchId) {
        String branchSequence = paymentService.processPayment(originBranchId, destinationBranchId);
        return ResponseEntity.ok(new ProcessPaymentResponseDto(branchSequence));
    }

}
