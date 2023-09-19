package com.example.merchantMicroService.Service.impl;

import com.example.merchantMicroService.Service.MerchantService;
import com.example.merchantMicroService.dto.MerchantProductPriceDTO;
import com.example.merchantMicroService.entity.Merchant;
import com.example.merchantMicroService.entity.ProductInventory;
import com.example.merchantMicroService.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    MerchantRepository merchantRepository;

    @Override
    public Iterable<Merchant> findMerchantList() {
        return merchantRepository.findAll();
    }

    @Override
    public void addOrUpdateMerchant(Merchant merchant) {
        merchantRepository.save(merchant);
    }

    @Override
    public void deleteMerchant(String emailId) {
        merchantRepository.deleteById(emailId);

    }

    @Override
    public Optional<Merchant> findById(String merchantEmailId) {
        return merchantRepository.findById(merchantEmailId);
    }

    public List<MerchantProductPriceDTO> showPriceMerchantStock(String productId) {
        ArrayList<MerchantProductPriceDTO> merchantProductPriceDTOArrayList=new ArrayList<>();
        List<Merchant> merchantList= new ArrayList<>();
        merchantList =merchantRepository.findAll();
        for(Merchant merchant:merchantList){
            List<ProductInventory> productInventoryList= new ArrayList<>();
            productInventoryList=merchant.getProductInventories();
            for (ProductInventory productInventory: productInventoryList){
                if(productInventory.getProductId().equals(productId)){
                    MerchantProductPriceDTO merchantProductPriceDTO=new MerchantProductPriceDTO();
                    merchantProductPriceDTO.setMerchantId(merchant.getId());
                    merchantProductPriceDTO.setProductPrice(productInventory.getPrice());
                    merchantProductPriceDTO.setProductStock(productInventory.getStock());
                    merchantProductPriceDTOArrayList.add(merchantProductPriceDTO);
                }
            }
        }
        Collections.sort(merchantProductPriceDTOArrayList, new Comparator<MerchantProductPriceDTO>() {
            @Override
            public int compare(MerchantProductPriceDTO dto, MerchantProductPriceDTO dto1) {
                return dto1.getProductPrice().compareTo(dto.getProductPrice());
            }
        });
        return merchantProductPriceDTOArrayList;
    }
    public Integer getStocks(String merchantId,String productId) {
        Optional<Merchant> optionalMerchant = merchantRepository.findById(merchantId);
        Merchant merchant = optionalMerchant.get();
        List<ProductInventory> productInventoryList = new ArrayList<>();
        Integer stock=0;
        productInventoryList = merchant.getProductInventories();
        for (ProductInventory productInventory : productInventoryList) {
            if (productInventory.getProductId().equals(productId)) {
                stock = productInventory.getStock();
                break;
            }
        }
        return stock;
    }
    public Integer getPrice(String merchantId,String productId) {
        Optional<Merchant> optionalMerchant = merchantRepository.findById(merchantId);
        Merchant merchant = optionalMerchant.get();
        List<ProductInventory> productInventoryList = new ArrayList<>();
        productInventoryList = merchant.getProductInventories();
        Integer price=0;
        for (ProductInventory productInventory : productInventoryList) {
            if (productInventory.getProductId().equals(productId)) {
                price = productInventory.getPrice();
                break;
            }
        }
        return price;
    }
}
