package com.example.ecommerce.controllers;



import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.OrderItem;
import com.example.ecommerce.models.User;
import com.example.ecommerce.services.OrderItemService;
import com.example.ecommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ecommerce.services.UserService;
import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final OrderService orderService;


    @GetMapping
    public List<OrderItem> getCurrentUserCartItems(Principal principal) {
        User currentUser = userService.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Order cart = orderService.getOrCreateCartForUser(currentUser);
        return orderItemService.getItemsByOrderId(cart.getId());
    }

    @PostMapping
    public OrderItem addProductToCart(@RequestBody OrderItem orderItem, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Order cart = orderService.getOrCreateCartForUser(currentUser);
        return orderItemService.addItemToOrder(cart.getId(), orderItem);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<OrderItem> updateCartItemQuantity(@PathVariable Long itemId, @RequestBody OrderItem itemDetails, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        try {
            OrderItem updatedItem = orderItemService.updateItemQuantity(itemId, itemDetails.getQuantity(), currentUser);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long itemId, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        try {
            orderItemService.deleteItem(itemId, currentUser);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}











