package com.abhilash.payment_service.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequestDto {
    private UUID originBranchId;
    private UUID destinationBranchId;
}
