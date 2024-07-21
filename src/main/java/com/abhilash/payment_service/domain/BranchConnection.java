package com.abhilash.payment_service.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Branch_Connection")
public class BranchConnection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(targetEntity = Branch.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "origin_branch_id", referencedColumnName = "id", nullable = false)
    private Branch originBranch;

    @ManyToOne(targetEntity = Branch.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "destination_branch_id", referencedColumnName = "id", nullable = false)
    private Branch destinationBranch;

}
