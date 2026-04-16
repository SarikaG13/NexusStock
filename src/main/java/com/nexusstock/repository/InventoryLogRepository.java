package com.nexusstock.repository;

import com.nexusstock.entity.InventoryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {

    Page<InventoryLog> findByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);

    List<InventoryLog> findTop20ByOrderByCreatedAtDesc();

    @Query("SELECT l FROM InventoryLog l WHERE l.createdAt >= :since ORDER BY l.createdAt DESC")
    List<InventoryLog> findRecentLogs(@Param("since") LocalDateTime since);

    @Query("SELECT l.actionType, COUNT(l) FROM InventoryLog l WHERE l.createdAt >= :since GROUP BY l.actionType")
    List<Object[]> countByActionTypeSince(@Param("since") LocalDateTime since);
}
