package com.nexusstock.service;

import com.nexusstock.dto.DashboardStats;
import com.nexusstock.dto.StockUpdateRequest;
import com.nexusstock.entity.InventoryLog;
import com.nexusstock.entity.InventoryLog.ActionType;
import com.nexusstock.entity.Product;
import com.nexusstock.repository.InventoryLogRepository;
import com.nexusstock.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryLogRepository inventoryLogRepository;

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsPaged(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Transactional
    public Product createProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new RuntimeException("Product with SKU '" + product.getSku() + "' already exists");
        }

        Product saved = productRepository.save(product);

        if (saved.getCurrentStock() > 0) {
            logInventoryChange(saved, saved.getCurrentStock(), ActionType.INITIAL);
        }

        log.info("Created product: {} (SKU: {})", saved.getName(), saved.getSku());
        return saved;
    }

    @Transactional
    public Product updateProduct(Long id, Product updates) {
        Product existing = getProductById(id);
        existing.setName(updates.getName());
        existing.setMinStockLevel(updates.getMinStockLevel());
        return productRepository.save(existing);
    }

    @Transactional
    public Product updateStock(Long id, StockUpdateRequest request) {
        Product product = getProductById(id);

        int newStock = switch (request.getActionType()) {
            case STOCK_IN -> product.getCurrentStock() + request.getChangeAmount();
            case STOCK_OUT -> {
                int result = product.getCurrentStock() - request.getChangeAmount();
                if (result < 0) {
                    throw new RuntimeException("Insufficient stock. Available: " + product.getCurrentStock());
                }
                yield result;
            }
            case ADJUSTMENT -> request.getChangeAmount();
            case INITIAL -> request.getChangeAmount();
        };

        product.setCurrentStock(newStock);
        Product saved = productRepository.save(product);

        logInventoryChange(saved, request.getChangeAmount(), request.getActionType());

        log.info("Stock updated for {}: {} units ({})", saved.getSku(), request.getChangeAmount(), request.getActionType());
        return saved;
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
        log.info("Deleted product: {} (SKU: {})", product.getName(), product.getSku());
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    @Transactional(readOnly = true)
    public List<InventoryLog> getRecentActivity() {
        return inventoryLogRepository.findTop20ByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public DashboardStats getDashboardStats() {
        LocalDateTime last24h = LocalDateTime.now().minusHours(24);
        return DashboardStats.builder()
                .totalProducts(productRepository.count())
                .totalStockUnits(productRepository.totalStockUnits())
                .lowStockCount(productRepository.countLowStockProducts())
                .recentActivityCount(inventoryLogRepository.findRecentLogs(last24h).size())
                .build();
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getActivityBreakdown() {
        LocalDateTime last7days = LocalDateTime.now().minusDays(7);
        List<Object[]> results = inventoryLogRepository.countByActionTypeSince(last7days);
        return results.stream()
                .collect(Collectors.toMap(
                        r -> ((ActionType) r[0]).name(),
                        r -> (Long) r[1]
                ));
    }

    private void logInventoryChange(Product product, int changeAmount, ActionType actionType) {
        InventoryLog logEntry = InventoryLog.builder()
                .product(product)
                .changeAmount(changeAmount)
                .actionType(actionType)
                .build();
        inventoryLogRepository.save(logEntry);
    }
}
