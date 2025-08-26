package com.example.ecommerce.services;

import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order getOrCreateCartForUser(User user) {
        Optional<Order> existingCart = orderRepository.findByUserAndStatus(user, "CART");

        if (existingCart.isPresent()) {
            return existingCart.get();
        } else {
            Order newCart = new Order();
            newCart.setUser(user);
            newCart.setStatus("CART");
            return orderRepository.save(newCart);
        }
    }

    @Transactional
    public Order checkout(User user) {
        Order cart = getOrCreateCartForUser(user);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Ваша корзина пуста");
        }

        cart.setStatus("PROCESSING");
        cart.setCreatedAt(java.time.LocalDateTime.now());

        return orderRepository.save(cart);
    }
}