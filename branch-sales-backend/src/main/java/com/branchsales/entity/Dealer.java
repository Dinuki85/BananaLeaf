package com.branchsales.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dealer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dealer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
