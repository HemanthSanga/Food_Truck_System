# Roam — Food Truck Ordering

A Java 25 + Spring Boot web application for discovering nearby food trucks, browsing their menus, placing pickup orders, and managing a truck's location, menu, and incoming orders.

## Run locally

Requirements: Java 25 or newer and Maven 3.6.3 or newer.

```powershell
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080) in a browser. The app starts with three Austin demo trucks and sample menus. No external database or API key is needed.

## Use the app

- **Discover:** Trucks are sorted by distance from the entered coordinates. Use **Find trucks** to search coordinates, or choose **Use current location** and allow browser location access.
- **Order:** Choose a truck, add menu items, enter a pickup name, and place the order. Payment is collected at pickup.
- **For sellers:** Select a demo truck to update its address and coordinates, add or remove menu items, and move incoming orders through received, preparing, ready, and completed.

## API

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/api/trucks?latitude={lat}&longitude={lon}` | List trucks ordered by distance |
| `GET` | `/api/trucks/{truckId}` | Get a truck and its menu |
| `POST` | `/api/trucks/{truckId}/menu` | Add a menu item |
| `PUT` | `/api/trucks/{truckId}/menu/{itemId}` | Update a menu item |
| `DELETE` | `/api/trucks/{truckId}/menu/{itemId}` | Remove a menu item |
| `PUT` | `/api/trucks/{truckId}/location` | Update a truck's location |
| `POST` | `/api/orders` | Place an order |
| `GET` | `/api/orders?truckId={truckId}` | List orders, optionally for one truck |
| `PATCH` | `/api/orders/{orderId}/status` | Update an order status |

Truck locations and customer coordinates use decimal latitude/longitude. Order and menu data are held in memory for this demo and reset when the server restarts. The seller screen is a prototype without authentication; add authentication and persistent storage before deploying it for real customers.
