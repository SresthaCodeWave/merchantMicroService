package com.example.merchantMicroService.controller;

import com.example.merchantMicroService.Feign.FeignInterface;
import com.example.merchantMicroService.Service.MerchantService;
import com.example.merchantMicroService.dto.MerchantDTO;
import com.example.merchantMicroService.dto.MerchantProductPriceDTO;
import com.example.merchantMicroService.dto.ProductDTO;
import com.example.merchantMicroService.entity.Merchant;
import com.example.merchantMicroService.entity.Product;
import com.example.merchantMicroService.entity.ProductInventory;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping(value = "/merchant")
public class MerchantController {
    @Autowired
    MerchantService merchantService;
    @Autowired
    FeignInterface feignInterface;
    @PostMapping("/registerMerchant/{merchantId}/{merchantName}/{password}")
    public ResponseEntity<Boolean> registerMerchant(@PathVariable("merchantId") String merchantId,@PathVariable("merchantName") String merchantName,@PathVariable("password") String password){
        Merchant merchant=new Merchant();
        merchant.setName(merchantName);
        merchant.setId(merchantId);
        merchant.setPassword(password);
        merchant.setProductInventories(Collections.emptyList());
        merchantService.addOrUpdateMerchant(merchant);
        return new ResponseEntity<Boolean>(true,HttpStatus.CREATED);
    }
    @PostMapping("/addOrUpdateMerchant")
    public ResponseEntity<String> addOrUpdateMerchant(@RequestBody MerchantDTO merchantDTO)
    {
        Merchant merchant=new Merchant();
        BeanUtils.copyProperties(merchantDTO,merchant);
        merchantService.addOrUpdateMerchant(merchant);

        return new ResponseEntity<String>(merchant.getId(),HttpStatus.CREATED);
    }
    @DeleteMapping("/deleteMerchant/{emailId}")
    public ResponseEntity<Boolean> deleteMerchant(@PathVariable("emailId") String emailId)
    {
        merchantService.deleteMerchant(emailId);

        return new ResponseEntity<Boolean>(Boolean.TRUE,HttpStatus.CREATED);
    }
    @GetMapping("/getMerchantById/{emailId}")
    public ResponseEntity<Object> findMerchant(@PathVariable("emailId") String emailId){
        Optional<Merchant> merchant = merchantService.findById(emailId);
        MerchantDTO merchantDTO = new MerchantDTO();
        if(merchant.isPresent()){
            BeanUtils.copyProperties(merchant.get(),merchantDTO);
            return new ResponseEntity<Object>(merchant,HttpStatus.OK);
        }
        else
            return new ResponseEntity<Object>("No data found for this identifier",HttpStatus.OK);
    }
    @GetMapping("/MerchantList")
    public ResponseEntity<ArrayList<Merchant>> getAllMerchants(){
        ArrayList<Merchant>   merchantsArrayList=new ArrayList<Merchant>();
        Iterable<Merchant> merchantIterable=merchantService.findMerchantList();
        for(Merchant merchant:merchantIterable){
            merchantsArrayList.add(merchant);
        }
        return new ResponseEntity<ArrayList<Merchant>>(merchantsArrayList,HttpStatus.OK);
    }
    @PutMapping("/updateStocks/{merchantId}/{productId}/{quantity}")
    public ResponseEntity<Boolean> updateStocks(@PathVariable("merchantId") String merchantEmailId,@PathVariable("productId") String productId,@PathVariable("quantity") Integer quantity) {
        Optional<Merchant> optionalMerchant =merchantService.findById(merchantEmailId);
        Merchant merchant = optionalMerchant.get();
        List<ProductInventory> productInventoryList = merchant.getProductInventories();
        for(ProductInventory productInventory:productInventoryList){
            if((productInventory.getProductId()).equals(productId)){
                if ((productInventory.getStock()+quantity)>0) {
                    productInventory.setStock(productInventory.getStock() + quantity);
                }
                else{
                    productInventory.setStock(0);
                }
                break;

            }
        }
        merchantService.addOrUpdateMerchant(merchant);
            return new ResponseEntity<Boolean>(Boolean.TRUE,HttpStatus.OK);
    }
    @PutMapping("/addExsistingProduct/{merchantId}/{productId}/{quantity}/{price}")
    public ResponseEntity<Boolean> addExsistingProduct(@PathVariable("merchantId") String merchantEmailId,@PathVariable("productId") String productId,@PathVariable("quantity") Integer quantity,@PathVariable("price") Integer price){
        Optional<Merchant> optionalMerchant =merchantService.findById(merchantEmailId);
        Merchant merchant = optionalMerchant.get();
        List<ProductInventory> productInventoryList = merchant.getProductInventories();
        ProductInventory productInventory=new ProductInventory();
        productInventory.setProductId(productId);
        productInventory.setStock(quantity);
        productInventory.setPrice(price);
        productInventoryList.add(productInventory);
        merchant.setProductInventories(productInventoryList);
        merchantService.addOrUpdateMerchant(merchant);
        return new ResponseEntity<Boolean>(Boolean.TRUE,HttpStatus.OK);
    }
    @PostMapping("/addNewItem/{merchantId}/{price}/{quantity}/{price}/{brand}/{productName}/{description}")
    public ResponseEntity<Boolean> addaNewProduct(@PathVariable("merchantId") String merchantId,@PathVariable("quantity") Integer quantity,@RequestParam String imgSrc,@PathVariable("description") String description,@PathVariable("productName") String productName,@PathVariable("brand") String brand,@PathVariable("price") Integer price){
        Product product =new Product();
        product.setBrand(brand);
        product.setDescription(description);
        product.setBrand(brand);
        product.setImgSrc(imgSrc);
        product.setProductName(productName);
        ProductDTO productDTO=new ProductDTO();
        BeanUtils.copyProperties(product,productDTO);
        String productId=feignInterface.addProduct(productDTO);
        Optional<Merchant> optionalMerchant =merchantService.findById(merchantId);
        Merchant merchant = optionalMerchant.get();
        List<ProductInventory> productInventoryList = merchant.getProductInventories();
        ProductInventory productInventory=new ProductInventory();
        productInventory.setProductId(productId);
        productInventory.setStock(quantity);
        productInventory.setPrice(price);
        productInventoryList.add(productInventory);
        merchant.setProductInventories(productInventoryList);
        merchantService.addOrUpdateMerchant(merchant);
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @GetMapping("/allProductsSoldByMerchant/{merchantId}")
    public ResponseEntity<List<ProductInventory>> allProductSoldByMerchant(@PathVariable("merchantId") String merchantId){
        Optional<Merchant> optionalMerchant= merchantService.findById(merchantId);
        Merchant merchant=optionalMerchant.get();
        List<ProductInventory> productInventoryList=merchant.getProductInventories();
        return new ResponseEntity<List<ProductInventory>>(productInventoryList,HttpStatus.OK);
    }
//    @GetMapping("/allMerchantsSellingTheParticularProduct")
//    public List<Me>
    @DeleteMapping("/deleteProduct/{merchantId}/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("merchantId") String merchantEmailId,@PathVariable("productId") String productId){
        Optional<Merchant> optionalMerchant =merchantService.findById(merchantEmailId);
        Merchant merchant = optionalMerchant.get();
        List<ProductInventory> productInventoryList = merchant.getProductInventories();
        for(ProductInventory productInventory:productInventoryList){
            if((productInventory.getProductId()).equals(productId)){
                productInventoryList.remove(productInventory);
                merchant.setProductInventories(productInventoryList);
                merchantService.addOrUpdateMerchant(merchant);
                break;
            }
        }
        merchant.setProductInventories(productInventoryList);
        return new ResponseEntity<Boolean>(Boolean.TRUE,HttpStatus.OK);
    }
    @GetMapping("getMerchantsDetailsForSameProduct/{productId}")
    public ResponseEntity<List<MerchantProductPriceDTO>> getPriceStockForProduct(@PathVariable("productId") String productId){
        List<MerchantProductPriceDTO> merchantProductPriceDTOS = new ArrayList<>();
        return new ResponseEntity<List<MerchantProductPriceDTO>>(merchantService.showPriceMerchantStock(productId),HttpStatus.OK);
    }
    @GetMapping("getstocks/{merchantId}/{productId}")
    public  ResponseEntity<Integer> getStocks(@PathVariable("merchantId") String merchantId,@PathVariable("productId") String productId){
        return new ResponseEntity<Integer>(merchantService.getStocks(merchantId,productId),HttpStatus.OK);
    }

    @GetMapping("getPrice/{merchantId}/{productId}")
    public  ResponseEntity<Integer> getPrice(@PathVariable("merchantId") String merchantId,@PathVariable("productId") String productId){
        return new ResponseEntity<Integer>(merchantService.getPrice(merchantId,productId),HttpStatus.OK);
    }
    @GetMapping("getProductPrice/{productId}")
    public ResponseEntity<Integer> getProductPrice(@PathVariable("productId") String productId){
        List<MerchantProductPriceDTO> merchantProductPriceDTOS = merchantService.showPriceMerchantStock(productId);
        Integer price=99999999;
        for (MerchantProductPriceDTO merchantProductPriceDTO:merchantProductPriceDTOS){
           if(price>merchantProductPriceDTO.getProductPrice()){
               price=merchantProductPriceDTO.getProductPrice();
           }
        }
        return new ResponseEntity<Integer>(price,HttpStatus.OK);
    }
    @GetMapping("getMerchantName/{merchantId}")
    public  ResponseEntity<String> getMerchantName(@PathVariable("merchantId") String merchantId){
        Optional<Merchant> optionalMerchant=merchantService.findById(merchantId);
        Merchant merchant=optionalMerchant.get();
        return new ResponseEntity<String>(merchant.getName(),HttpStatus.OK);
    }

}
