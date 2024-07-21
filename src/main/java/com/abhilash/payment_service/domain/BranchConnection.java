package com.abhilash.payment_service.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "Branch_Connection")
public class BranchConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(targetEntity = Branch.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "origin_branch_id", referencedColumnName = "id", nullable = false)
    private Branch originBranch;

    @ManyToOne(targetEntity = Branch.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "destination_branch_id", referencedColumnName = "id", nullable = false)
    private Branch destinationBranch;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Long version;

}
