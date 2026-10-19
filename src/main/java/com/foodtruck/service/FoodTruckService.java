package com.foodtruck.service;

import com.foodtruck.api.ApiModels.CreateOrderRequest;
import com.foodtruck.api.ApiModels.MenuItemRequest;
import com.foodtruck.api.ApiModels.MenuItemResponse;
import com.foodtruck.api.ApiModels.OrderLineRequest;
import com.foodtruck.api.ApiModels.OrderLineResponse;
import com.foodtruck.api.ApiModels.OrderResponse;
import com.foodtruck.api.ApiModels.TruckLocationRequest;
import com.foodtruck.api.ApiModels.TruckResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FoodTruckService {
    private static final double EARTH_RADIUS_KM = 6371.0088;

    private final Map<String, Truck> trucks = new LinkedHashMap<>();
    private final Map<String, OrderResponse> orders = new LinkedHashMap<>();
    private final AtomicLong menuItemIds = new AtomicLong(1);
    private final AtomicLong orderIds = new AtomicLong(1);

    public FoodTruckService() {
        addDemoTruck(
                "truck-1",
                "Juniper & Smoke",
                "Texas BBQ",
                "Slow-smoked favorites, made fresh and served with a little Southern comfort.",
                "South Congress Ave, Austin, TX",
                30.2505,
                -97.7490,
                "#df7049",
                new MenuItemRequest("Brisket sandwich", "14-hour smoked brisket, pickles, house BBQ.", new BigDecimal("13.50"), "🥪"),
                new MenuItemRequest("Smoked mac & cheese", "Creamy cheddar mac with a smoky breadcrumb crust.", new BigDecimal("7.00"), "🧀"),
                new MenuItemRequest("Peach iced tea", "Fresh-brewed black tea with Texas peach.", new BigDecimal("3.50"), "🍑"));
        addDemoTruck(
                "truck-2",
                "Little Olive",
                "Mediterranean",
                "Bright, hearty bowls and wraps inspired by the Mediterranean coast.",
                "Zilker Park, Austin, TX",
                30.2669,
                -97.7729,
                "#8a9e68",
                new MenuItemRequest("Falafel pita", "Crispy falafel, hummus, cucumber, tomato, and herbs.", new BigDecimal("11.00"), "🥙"),
                new MenuItemRequest("Lemon herb bowl", "Rice, roasted vegetables, feta, and lemon tahini.", new BigDecimal("12.50"), "🥗"),
                new MenuItemRequest("Rose lemonade", "House lemonade with a hint of rose.", new BigDecimal("4.00"), "🍋"));
        addDemoTruck(
                "truck-3",
                "Miso Hungry",
                "Japanese street food",
                "Comforting Japanese street food with bold sauces and plenty of crunch.",
                "East 6th Street, Austin, TX",
                30.2672,
                -97.7310,
                "#d6a33e",
                new MenuItemRequest("Chicken katsu sando", "Crispy chicken, cabbage, and tangy katsu sauce.", new BigDecimal("12.00"), "🍱"),
                new MenuItemRequest("Ginger edamame", "Warm edamame tossed with ginger and sea salt.", new BigDecimal("5.50"), "🫛"),
                new MenuItemRequest("Yuzu soda", "Sparkling citrus soda with a bright yuzu finish.", new BigDecimal("3.75"), "🥤"));
    }

    public synchronized List<TruckResponse> findTrucks(Double latitude, Double longitude) {
        return trucks.values().stream()
                .map(truck -> toTruckResponse(truck, latitude, longitude))
                .sorted(Comparator.comparing(
                        TruckResponse::distanceKm,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    public synchronized TruckResponse getTruck(String truckId) {
        return toTruckResponse(requireTruck(truckId), null, null);
    }

    public synchronized MenuItemResponse addMenuItem(String truckId, MenuItemRequest request) {
        Truck truck = requireTruck(truckId);
        validateMenuItem(request);
        MenuItem item = new MenuItem(
                "item-" + menuItemIds.getAndIncrement(),
                request.name().trim(),
                cleanOptionalText(request.description()),
                request.price().setScale(2, RoundingMode.HALF_UP),
                normalizeEmoji(request.emoji()));
        truck.menu.add(item);
        return toMenuItemResponse(item);
    }

    public synchronized MenuItemResponse updateMenuItem(String truckId, String itemId, MenuItemRequest request) {
        Truck truck = requireTruck(truckId);
        validateMenuItem(request);
        int itemIndex = findMenuItemIndex(truck, itemId);
        MenuItem item = new MenuItem(
                itemId,
                request.name().trim(),
                cleanOptionalText(request.description()),
                request.price().setScale(2, RoundingMode.HALF_UP),
                normalizeEmoji(request.emoji()));
        truck.menu.set(itemIndex, item);
        return toMenuItemResponse(item);
    }

    public synchronized void deleteMenuItem(String truckId, String itemId) {
        Truck truck = requireTruck(truckId);
        int itemIndex = findMenuItemIndex(truck, itemId);
        truck.menu.remove(itemIndex);
    }

    public synchronized TruckResponse updateTruckLocation(String truckId, TruckLocationRequest request) {
        Truck truck = requireTruck(truckId);
        if (request == null || request.address() == null || request.address().isBlank()) {
            throw badRequest("A truck address is required.");
        }
        validateCoordinates(request.latitude(), request.longitude());
        truck.address = request.address().trim();
        truck.latitude = request.latitude();
        truck.longitude = request.longitude();
        return toTruckResponse(truck, null, null);
    }

    public synchronized OrderResponse createOrder(CreateOrderRequest request) {
        if (request == null || request.customerName() == null || request.customerName().isBlank()) {
            throw badRequest("Please enter the customer's name.");
        }
        if (request.items() == null || request.items().isEmpty()) {
            throw badRequest("Add at least one food item before placing an order.");
        }

        Truck truck = requireTruck(request.truckId());
        Map<String, Integer> quantities = new LinkedHashMap<>();
        for (OrderLineRequest line : request.items()) {
            if (line == null || line.menuItemId() == null || line.quantity() < 1 || line.quantity() > 99) {
                throw badRequest("Each order item must have a valid menu item and quantity from 1 to 99.");
            }
            quantities.merge(line.menuItemId(), line.quantity(), Integer::sum);
        }

        List<OrderLineResponse> lines = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            MenuItem item = findMenuItem(truck, entry.getKey());
            BigDecimal lineTotal = item.price().multiply(BigDecimal.valueOf(entry.getValue()));
            lines.add(new OrderLineResponse(
                    item.id(),
                    item.name(),
                    item.price(),
                    entry.getValue(),
                    lineTotal));
            total = total.add(lineTotal);
        }

        String orderId = "order-" + orderIds.getAndIncrement();
        OrderResponse order = new OrderResponse(
                orderId,
                truck.id,
                truck.name,
                request.customerName().trim(),
                List.copyOf(lines),
                total.setScale(2, RoundingMode.HALF_UP),
                "RECEIVED",
                Instant.now());
        orders.put(orderId, order);
        return order;
    }

    public synchronized List<OrderResponse> findOrders(String truckId) {
        if (truckId != null && !truckId.isBlank()) {
            requireTruck(truckId);
        }
        return orders.values().stream()
                .filter(order -> truckId == null || truckId.isBlank() || order.truckId().equals(truckId))
                .sorted(Comparator.comparing(OrderResponse::createdAt).reversed())
                .toList();
    }

    public synchronized OrderResponse updateOrderStatus(String orderId, String requestedStatus) {
        if (requestedStatus == null || !List.of("RECEIVED", "PREPARING", "READY", "COMPLETED", "CANCELLED")
                .contains(requestedStatus.trim().toUpperCase())) {
            throw badRequest("Choose a valid order status.");
        }
        OrderResponse current = orders.get(orderId);
        if (current == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
        }
        OrderResponse updated = new OrderResponse(
                current.id(),
                current.truckId(),
                current.truckName(),
                current.customerName(),
                current.items(),
                current.total(),
                requestedStatus.trim().toUpperCase(),
                current.createdAt());
        orders.put(orderId, updated);
        return updated;
    }

    private void addDemoTruck(
            String id,
            String name,
            String cuisine,
            String description,
            String address,
            double latitude,
            double longitude,
            String accent,
            MenuItemRequest... menu) {
        Truck truck = new Truck(id, name, cuisine, description, address, latitude, longitude, accent);
        for (MenuItemRequest item : menu) {
            truck.menu.add(new MenuItem(
                    "item-" + menuItemIds.getAndIncrement(),
                    item.name(),
                    item.description(),
                    item.price(),
                    item.emoji()));
        }
        trucks.put(id, truck);
    }

    private TruckResponse toTruckResponse(Truck truck, Double latitude, Double longitude) {
        Double distance = latitude == null || longitude == null
                ? null
                : haversineKm(latitude, longitude, truck.latitude, truck.longitude);
        return new TruckResponse(
                truck.id,
                truck.name,
                truck.cuisine,
                truck.description,
                truck.address,
                truck.latitude,
                truck.longitude,
                truck.accent,
                truck.menu.stream().map(this::toMenuItemResponse).toList(),
                distance);
    }

    private MenuItemResponse toMenuItemResponse(MenuItem item) {
        return new MenuItemResponse(item.id(), item.name(), item.description(), item.price(), item.emoji());
    }

    private void validateMenuItem(MenuItemRequest request) {
        if (request == null || request.name() == null || request.name().isBlank()) {
            throw badRequest("A food item name is required.");
        }
        if (request.name().trim().length() > 80) {
            throw badRequest("Food item names must be 80 characters or fewer.");
        }
        if (request.description() != null && request.description().length() > 240) {
            throw badRequest("Descriptions must be 240 characters or fewer.");
        }
        if (request.price() == null || request.price().compareTo(BigDecimal.ZERO) <= 0
                || request.price().compareTo(new BigDecimal("9999.99")) > 0) {
            throw badRequest("Enter a price greater than $0 and no more than $9,999.99.");
        }
    }

    private int findMenuItemIndex(Truck truck, String itemId) {
        for (int index = 0; index < truck.menu.size(); index++) {
            if (truck.menu.get(index).id().equals(itemId)) {
                return index;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item not found.");
    }

    private MenuItem findMenuItem(Truck truck, String itemId) {
        return truck.menu.stream()
                .filter(item -> item.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> badRequest("One or more selected food items are no longer on this menu."));
    }

    private Truck requireTruck(String truckId) {
        Truck truck = trucks.get(truckId);
        if (truck == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Food truck not found.");
        }
        return truck;
    }

    private void validateCoordinates(double latitude, double longitude) {
        if (!Double.isFinite(latitude) || !Double.isFinite(longitude)
                || latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw badRequest("Enter a valid latitude and longitude.");
        }
    }

    private double haversineKm(double latitude, double longitude, double otherLatitude, double otherLongitude) {
        validateCoordinates(latitude, longitude);
        double latitudeDelta = Math.toRadians(otherLatitude - latitude);
        double longitudeDelta = Math.toRadians(otherLongitude - longitude);
        double a = Math.pow(Math.sin(latitudeDelta / 2), 2)
                + Math.cos(Math.toRadians(latitude))
                * Math.cos(Math.toRadians(otherLatitude))
                * Math.pow(Math.sin(longitudeDelta / 2), 2);
        return EARTH_RADIUS_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private String cleanOptionalText(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeEmoji(String emoji) {
        return emoji == null || emoji.isBlank() ? "🍽️" : emoji.trim();
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    private static final class Truck {
        private final String id;
        private final String name;
        private final String cuisine;
        private final String description;
        private String address;
        private double latitude;
        private double longitude;
        private final String accent;
        private final List<MenuItem> menu = new ArrayList<>();

        private Truck(
                String id,
                String name,
                String cuisine,
                String description,
                String address,
                double latitude,
                double longitude,
                String accent) {
            this.id = id;
            this.name = name;
            this.cuisine = cuisine;
            this.description = description;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
            this.accent = accent;
        }
    }

    private record MenuItem(String id, String name, String description, BigDecimal price, String emoji) {
    }
}
