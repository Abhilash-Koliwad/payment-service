package com.abhilash.payment_service.service;

import java.util.UUID;

public interface IPaymentService {

    String processPayment(UUID originBranchId, UUID destinationBranchId);

}
