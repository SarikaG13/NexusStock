package com.nexusstock.config;

import com.nexusstock.entity.InventoryLog.ActionType;
import com.nexusstock.entity.Product;
import com.nexusstock.dto.StockUpdateRequest;
import com.nexusstock.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductService productService;

    @Override
    public void run(String... args) {
        if (productService.getAllProducts().isEmpty()) {
            log.info("Seeding demo data...");

            createProduct("MacBook Pro 16\"", "MBP-16-M3", 45, 10);
            createProduct("Dell XPS 15", "DELL-XPS15", 32, 15);
            createProduct("Logitech MX Master 3S", "LOG-MXM3S", 120, 25);
            createProduct("Samsung 34\" Ultrawide", "SAM-34UW", 8, 10);
            createProduct("Sony WH-1000XM5", "SONY-XM5", 65, 20);
            createProduct("Apple Magic Keyboard", "APL-MGKB", 5, 15);
            createProduct("Corsair K95 RGB", "COR-K95", 28, 10);
            createProduct("Raspberry Pi 5", "RPI-5-8GB", 200, 50);
            createProduct("Arduino Mega 2560", "ARD-MEGA", 3, 20);
            createProduct("USB-C Hub 10-in-1", "USB-HUB10", 150, 30);

            log.info("Demo data seeded: 10 products created");
        }
    }

    private void createProduct(String name, String sku, int stock, int minLevel) {
        Product p = Product.builder()
                .name(name)
                .sku(sku)
                .currentStock(stock)
                .minStockLevel(minLevel)
                .build();
        productService.createProduct(p);
    }
}
