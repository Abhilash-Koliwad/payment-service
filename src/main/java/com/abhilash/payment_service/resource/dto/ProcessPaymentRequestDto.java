package com.abhilash.payment_service.resource.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequestDto {
    @NotNull(message = "Origin branch ID cannot be null")
    private UUID originBranchId;
    @NotNull(message = "Destination branch ID cannot be null")
    private UUID destinationBranchId;
}
