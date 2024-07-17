package com.abhilash.payment_service.repository;

import com.abhilash.payment_service.domain.BranchConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BranchConnectionRepository extends JpaRepository<BranchConnection, UUID> {
}
