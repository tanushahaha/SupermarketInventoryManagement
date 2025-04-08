package com.supermarketims.repository;

import com.supermarketims.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    // Find cart items for a specific user
    List<Cart> findByUserId(Long userId);

    // ✅ Find a specific cart item by user and item
    Optional<Cart> findByUserIdAndItemId(Long userId, Long itemId);
    
    @Transactional // ✅ Ensures transactional behavior
    void deleteByUserIdAndItemId(Long userId, Long itemId);

    @Transactional // ✅ Ensures deleteAll() runs inside a transaction
    void deleteByUserId(Long userId);
}
