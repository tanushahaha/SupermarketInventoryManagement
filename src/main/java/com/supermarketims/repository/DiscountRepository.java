package com.supermarketims.repository;

import com.supermarketims.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    // Get all active discounts for a given item
    List<Discount> findByItemIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
        Long itemId,
        LocalDate currentDate1,
        LocalDate currentDate2
    );
}