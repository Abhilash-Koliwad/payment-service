package com.abhilash.payment_service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "Branch_Connection")
@AllArgsConstructor
@NoArgsConstructor
public class BranchConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
