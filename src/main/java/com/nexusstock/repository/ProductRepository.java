package com.nexusstock.repository;

import com.nexusstock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    @Query("SELECT p FROM Product p WHERE p.currentStock <= p.minStockLevel")
    List<Product> findLowStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.currentStock <= p.minStockLevel")
    long countLowStockProducts();

    @Query("SELECT COALESCE(SUM(p.currentStock), 0) FROM Product p")
    long totalStockUnits();
}
