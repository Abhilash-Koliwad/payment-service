package com.abhilash.payment_service.repository;

import com.abhilash.payment_service.domain.BranchConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchConnectionRepository extends JpaRepository<BranchConnection, UUID> {

    Optional<BranchConnection> findByOriginBranchIdAndDestinationBranchId(UUID originBranchId, UUID destinationBranchId);

    List<BranchConnection> findByOriginBranchId(UUID originBranchId);

}
