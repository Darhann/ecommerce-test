package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class AddItemRequest {
    private Long productId;
    private int quantity;
}