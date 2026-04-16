package com.nexusstock;

import com.nexusstock.dto.DashboardStats;
import com.nexusstock.dto.StockUpdateRequest;
import com.nexusstock.entity.InventoryLog;
import com.nexusstock.entity.InventoryLog.ActionType;
import com.nexusstock.entity.Product;
import com.nexusstock.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class NexusStockApplicationTests {

    @Autowired
    private ProductService productService;

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateProduct() {
        Product p = Product.builder()
                .name("Test Widget")
                .sku("TST-001")
                .currentStock(50)
                .minStockLevel(10)
                .build();
        Product saved = productService.createProduct(p);
        assertNotNull(saved.getId());
        assertEquals("Test Widget", saved.getName());
        assertEquals("TST-001", saved.getSku());
        assertEquals(50, saved.getCurrentStock());
    }

    @Test
    void testDuplicateSkuThrows() {
        Product p1 = Product.builder().name("A").sku("DUP-001").currentStock(10).minStockLevel(5).build();
        productService.createProduct(p1);
        Product p2 = Product.builder().name("B").sku("DUP-001").currentStock(5).minStockLevel(5).build();
        assertThrows(RuntimeException.class, () -> productService.createProduct(p2));
    }

    @Test
    void testStockIn() {
        Product p = Product.builder().name("StockIn Test").sku("SI-001").currentStock(20).minStockLevel(5).build();
        Product saved = productService.createProduct(p);

        StockUpdateRequest req = new StockUpdateRequest(10, ActionType.STOCK_IN);
        Product updated = productService.updateStock(saved.getId(), req);
        assertEquals(30, updated.getCurrentStock());
    }

    @Test
    void testStockOutInsufficientThrows() {
        Product p = Product.builder().name("StockOut Test").sku("SO-001").currentStock(5).minStockLevel(2).build();
        Product saved = productService.createProduct(p);

        StockUpdateRequest req = new StockUpdateRequest(10, ActionType.STOCK_OUT);
        assertThrows(RuntimeException.class, () -> productService.updateStock(saved.getId(), req));
    }

    @Test
    void testDashboardStats() {
        DashboardStats stats = productService.getDashboardStats();
        assertNotNull(stats);
        assertTrue(stats.getTotalProducts() >= 0);
    }

    @Test
    void testLowStockDetection() {
        Product p = Product.builder().name("Low Stock").sku("LS-001").currentStock(3).minStockLevel(10).build();
        productService.createProduct(p);

        List<Product> lowStock = productService.getLowStockProducts();
        assertTrue(lowStock.stream().anyMatch(prod -> "LS-001".equals(prod.getSku())));
    }
}
