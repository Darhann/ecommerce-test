package com.example.ecommerce.repository;

import com.example.ecommerce.models.Product;
import org.springframework.data.domain.Page; // <-- Импортируем Page
import org.springframework.data.domain.Pageable; // <-- Импортируем Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}