package com.example.merchantMicroService.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String productId;
    private String productName;
    private String brand;
    private String description;
    private String imgSrc;
}
