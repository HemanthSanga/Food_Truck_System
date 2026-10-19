package com.foodtruck.service;

import com.foodtruck.api.ApiModels.CreateOrderRequest;
import com.foodtruck.api.ApiModels.OrderLineRequest;
import com.foodtruck.api.ApiModels.OrderResponse;
import com.foodtruck.api.ApiModels.TruckResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FoodTruckServiceTest {
    private final FoodTruckService service = new FoodTruckService();

    @Test
    void sortsTrucksByDistanceFromCustomerCoordinates() {
        List<TruckResponse> trucks = service.findTrucks(30.2672, -97.7431);

        assertEquals("truck-3", trucks.get(0).id());
        assertTrue(trucks.get(0).distanceKm() < trucks.get(1).distanceKm());
        assertTrue(trucks.get(1).distanceKm() < trucks.get(2).distanceKm());
    }

    @Test
    void createsOrderWithServerCalculatedTotalAndUpdatesItsStatus() {
        CreateOrderRequest request = new CreateOrderRequest(
                "truck-1",
                "Alex",
                List.of(new OrderLineRequest("item-1", 2), new OrderLineRequest("item-3", 1)));

        OrderResponse created = service.createOrder(request);
        OrderResponse updated = service.updateOrderStatus(created.id(), "preparing");

        assertEquals(new BigDecimal("30.50"), created.total());
        assertEquals("RECEIVED", created.status());
        assertEquals("PREPARING", updated.status());
        assertEquals(1, service.findOrders("truck-1").size());
    }

    @Test
    void rejectsOrdersWithInvalidQuantities() {
        CreateOrderRequest request = new CreateOrderRequest(
                "truck-1",
                "Alex",
                List.of(new OrderLineRequest("item-1", 0)));

        assertThrows(ResponseStatusException.class, () -> service.createOrder(request));
    }
}
