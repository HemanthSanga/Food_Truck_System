package com.foodtruck.api;

import com.foodtruck.api.ApiModels.CreateOrderRequest;
import com.foodtruck.api.ApiModels.MenuItemRequest;
import com.foodtruck.api.ApiModels.MenuItemResponse;
import com.foodtruck.api.ApiModels.OrderResponse;
import com.foodtruck.api.ApiModels.OrderStatusRequest;
import com.foodtruck.api.ApiModels.TruckLocationRequest;
import com.foodtruck.api.ApiModels.TruckResponse;
import com.foodtruck.service.FoodTruckService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FoodTruckController {
    private final FoodTruckService foodTruckService;

    public FoodTruckController(FoodTruckService foodTruckService) {
        this.foodTruckService = foodTruckService;
    }

    @GetMapping("/trucks")
    public List<TruckResponse> findTrucks(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        if ((latitude == null) != (longitude == null)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Provide both latitude and longitude.");
        }
        return foodTruckService.findTrucks(latitude, longitude);
    }

    @GetMapping("/trucks/{truckId}")
    public TruckResponse getTruck(@PathVariable String truckId) {
        return foodTruckService.getTruck(truckId);
    }

    @PostMapping("/trucks/{truckId}/menu")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse addMenuItem(
            @PathVariable String truckId,
            @RequestBody MenuItemRequest request) {
        return foodTruckService.addMenuItem(truckId, request);
    }

    @PutMapping("/trucks/{truckId}/menu/{itemId}")
    public MenuItemResponse updateMenuItem(
            @PathVariable String truckId,
            @PathVariable String itemId,
            @RequestBody MenuItemRequest request) {
        return foodTruckService.updateMenuItem(truckId, itemId, request);
    }

    @DeleteMapping("/trucks/{truckId}/menu/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuItem(@PathVariable String truckId, @PathVariable String itemId) {
        foodTruckService.deleteMenuItem(truckId, itemId);
    }

    @PutMapping("/trucks/{truckId}/location")
    public TruckResponse updateTruckLocation(
            @PathVariable String truckId,
            @RequestBody TruckLocationRequest request) {
        return foodTruckService.updateTruckLocation(truckId, request);
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        return foodTruckService.createOrder(request);
    }

    @GetMapping("/orders")
    public List<OrderResponse> findOrders(@RequestParam(required = false) String truckId) {
        return foodTruckService.findOrders(truckId);
    }

    @PatchMapping("/orders/{orderId}/status")
    public OrderResponse updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody OrderStatusRequest request) {
        return foodTruckService.updateOrderStatus(orderId, request.status());
    }
}
