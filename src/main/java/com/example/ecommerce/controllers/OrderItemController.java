package com.example.ecommerce.controllers;


import com.example.ecommerce.models.OrderItem;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/{orderId}/items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public List<OrderItem> getOrderItems(@PathVariable Long orderId) {
        return orderItemService.getItemsByOrderId(orderId);
    }

    @PostMapping
    public OrderItem addOrderItem(@PathVariable Long orderId, @RequestBody OrderItem orderItem) {
        return orderItemService.addItemToOrder(orderId, orderItem);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable Long orderId,
                                                     @PathVariable Long itemId,
                                                     @RequestBody OrderItem itemDetails) {

        // TODO дополнительная проверка через orderId
        try {
            OrderItem updatedItem = orderItemService.updateItemQuantity(itemId, itemDetails.getQuantity());
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        try {
            orderItemService.deleteItem(itemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}











