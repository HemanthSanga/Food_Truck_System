package com.foodtruck.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public final class ApiModels {
    private ApiModels() {
    }

    public record MenuItemRequest(String name, String description, BigDecimal price, String emoji) {
    }

    public record MenuItemResponse(
            String id,
            String name,
            String description,
            BigDecimal price,
            String emoji) {
    }

    public record TruckLocationRequest(String address, double latitude, double longitude) {
    }

    public record TruckResponse(
            String id,
            String name,
            String cuisine,
            String description,
            String address,
            double latitude,
            double longitude,
            String accent,
            List<MenuItemResponse> menu,
            Double distanceKm) {
    }

    public record OrderLineRequest(String menuItemId, int quantity) {
    }

    public record CreateOrderRequest(String truckId, String customerName, List<OrderLineRequest> items) {
    }

    public record OrderLineResponse(
            String menuItemId,
            String name,
            BigDecimal unitPrice,
            int quantity,
            BigDecimal lineTotal) {
    }

    public record OrderResponse(
            String id,
            String truckId,
            String truckName,
            String customerName,
            List<OrderLineResponse> items,
            BigDecimal total,
            String status,
            Instant createdAt) {
    }

    public record OrderStatusRequest(String status) {
    }
}
