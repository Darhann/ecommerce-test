package com.example.ecommerce.services;

import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.OrderItem;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private  final ProductRepository productRepository;


    public List<OrderItem> getItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Transactional
    public OrderItem addItemToOrder(Long orderId, OrderItem item) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        Product product = productRepository.findById(item.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + item.getProduct().getId()));

        if (product.getStock() < item.getQuantity()) {
            throw new RuntimeException("Недостаточно товара на складе. В налии: " + product.getStock());
        }

        product.setStock(product.getStock() - item.getQuantity());
        productRepository.save(product);

        item.setOrder(order);
        item.setProduct(product);
        item.setPrice(product.getPrice());

        return orderItemRepository.save(item);
    }

    @Transactional
    public OrderItem updateItemQuantity(Long itemId, int newQuantity, User currentUser) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Позиция в заказе не найдена с id: " + itemId));

        if (!Objects.equals(item.getOrder().getUser().getId(), currentUser.getId())) {
            throw new AccessDeniedException("Вы не можете изменять позиции в чужом заказе");
        }

        Product product = item.getProduct();
        int oldQuantity = item.getQuantity();

        product.setStock(product.getStock() + oldQuantity);

        if (product.getStock() < newQuantity) {
            product.setStock(product.getStock() - oldQuantity);
            throw new RuntimeException("Недостаточно товара на складе. В наличии: " + product.getStock());
        }

        product.setStock(product.getStock() - newQuantity);
        item.setQuantity(newQuantity);

        productRepository.save(product);
        return orderItemRepository.save(item);
    }

    @Transactional
    public void deleteItem(Long itemId, User currentUser) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Позиция в заказе не найдена с id: " + itemId));

        if (!Objects.equals(item.getOrder().getUser().getId(), currentUser.getId())) {
            throw new AccessDeniedException("Вы не можете удалять позиции из чужого заказа");
        }

        Product product = item.getProduct();
        product.setStock(product.getStock() + item.getQuantity());
        productRepository.save(product);

        orderItemRepository.deleteById(itemId);
    }
}
