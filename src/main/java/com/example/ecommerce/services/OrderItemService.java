package com.example.ecommerce.services;

import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.OrderItem;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private  final ProductRepository productRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<OrderItem> getItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public OrderItem addItemToOrder(Long orderId, OrderItem item) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        Product product = productRepository.findById(item.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + item.getProduct().getId()));

        item.setOrder(order);
        item.setProduct(product);
        // TODO добавить логику проверки остатков (stock) и расчета цены
        return orderItemRepository.save(item);
    }

    public OrderItem updateItemQuantity(Long itemId, int quantity, User currentUser) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + itemId));

        if (!Objects.equals(item.getOrder().getUser().getId(), currentUser.getId())) {
            throw new AccessDeniedException("Вы не можете изменять позиции в чужом заказе");
        }

        item.setQuantity(quantity);
        // TODO добавить логику пересчитывания цены
        return orderItemRepository.save(item);
    }

    public void deleteItem(Long itemId, User currentUser) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + itemId));

        if (!Objects.equals(item.getOrder().getUser().getId(), currentUser.getId())) {
            throw new AccessDeniedException("Вы не можете удалять позиции из чужого заказа");
        }

        orderItemRepository.deleteById(itemId);
    }
}
