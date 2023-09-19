package com.example.merchantMicroService.dto;


import com.example.merchantMicroService.entity.ProductInventory;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class MerchantDTO {
    private String id;
    private String name;
    private String password;
    private List<ProductInventory> productInventories;

}