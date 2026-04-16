package com.nexusstock.controller;

import com.nexusstock.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ProductService productService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("stats", productService.getDashboardStats());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("lowStockProducts", productService.getLowStockProducts());
        model.addAttribute("recentActivity", productService.getRecentActivity());
        model.addAttribute("activityBreakdown", productService.getActivityBreakdown());
        return "dashboard";
    }
}
