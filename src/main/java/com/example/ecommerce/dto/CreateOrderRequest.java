package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long productId;
    private int quantity;
}