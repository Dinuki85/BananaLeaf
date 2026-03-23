package com.branchsales.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MainCategory mainCategory;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "up")
    private Double unitPrice;

    @Column(name = "sp")
    private Double sellingPrice;

    @Column(name = "active")
    private String active;

    @Version
    private Long version;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
