package com.nexusstock.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = "sku"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "SKU is required")
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Min(value = 0, message = "Current stock cannot be negative")
    @Column(nullable = false)
    @Builder.Default
    private Integer currentStock = 0;

    @Min(value = 0, message = "Min stock level cannot be negative")
    @Column(nullable = false)
    @Builder.Default
    private Integer minStockLevel = 10;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
