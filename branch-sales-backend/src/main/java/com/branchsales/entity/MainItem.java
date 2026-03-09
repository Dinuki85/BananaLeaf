package com.branchsales.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "main_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmain_item")
    @JsonProperty("id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_idmain_category")
    private MainCategory mainCategory;

    @Column(name = "name")
    private String name;

    @Column(name = "sp")
    private Double price;

    @Column(name = "code")
    private String code;
}
