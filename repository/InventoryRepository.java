package com.supermarketims.repository;

import com.supermarketims.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Item, Long> {
    
    List<Item> findByNameContaining(String name);
    
    @Query("SELECT i FROM Item i WHERE i.category = (SELECT i2.category FROM Item i2 WHERE i2.id = :itemId) AND i.id <> :itemId")
    List<Item> findAlternatives(@Param("itemId") Long itemId);

    List<Item> findTop10ByOrderByIdAsc();
}
