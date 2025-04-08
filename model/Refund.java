package com.supermarketims.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "refunds")
@Getter
@Setter
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    private String reason;

    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    public enum RefundStatus {
        PENDING, APPROVED, REJECTED
    }
}
