package com.example.merchantMicroService.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@ToString
@Document(collection = "Merchant")
public class Merchant {
    @Id
    private String id;
    private String name;
    private String password;
    private List<ProductInventory> productInventories;

}