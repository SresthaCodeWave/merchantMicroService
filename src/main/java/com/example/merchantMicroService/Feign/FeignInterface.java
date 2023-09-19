package com.example.merchantMicroService.Feign;

import com.example.merchantMicroService.dto.ProductDTO;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;



@FeignClient(url = "localhost:8085/products",value ="prod")
public interface FeignInterface {
//    @GetMapping("/allProducts")
//    public ArrayList<Product> getAllProducts();
//    @GetMapping("/isProductExist")
//    public Boolean isProductExist(String productId);
    @PostMapping ("/addProduct")
    public String addProduct(ProductDTO productDTO);



}
