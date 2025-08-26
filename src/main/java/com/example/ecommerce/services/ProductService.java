package com.example.ecommerce.services;

import com.example.ecommerce.models.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getAllProducts(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice((productDetails.getPrice()));
        product.setStock(productDetails.getStock());
        return productRepository.save(product);
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
