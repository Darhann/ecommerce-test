package com.example.ecommerce.services;

import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.OrderItem;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public OrderItem updateItemQuantity(Long itemId, int quantity) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + itemId));
        item.setQuantity(quantity);
        // TODO добавить логику пересчитывания цены
        return orderItemRepository.save(item);
    }

    public void deleteItem(Long itemId) {
        if (!orderItemRepository.existsById(itemId)) {
            throw new RuntimeException("OrderItem not found with id: " + itemId);
        }
        orderItemRepository.deleteById(itemId);
    }
}
