package com.nexusstock.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer changeAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ActionType actionType;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum ActionType {
        STOCK_IN,
        STOCK_OUT,
        ADJUSTMENT,
        INITIAL
    }
}
