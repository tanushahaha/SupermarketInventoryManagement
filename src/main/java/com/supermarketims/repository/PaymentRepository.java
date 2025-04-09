package com.supermarketims.repository;

import com.supermarketims.model.Payment;
import com.supermarketims.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStatus(PaymentStatus status);
    Payment findByOrderId(Long orderId);
}
