package com.example.merchantMicroService.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document

public class ProductInventory {
    private String productId;
    private Integer stock;
    private Integer price;
}
