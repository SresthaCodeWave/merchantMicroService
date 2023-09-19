package com.example.merchantMicroService.Service;


import com.example.merchantMicroService.dto.MerchantProductPriceDTO;
import com.example.merchantMicroService.entity.Merchant;

import java.util.List;
import java.util.Optional;

public interface MerchantService {
    public Iterable<Merchant> findMerchantList();

    public void addOrUpdateMerchant(Merchant merchant);

    public void deleteMerchant(String merchantId);

    public Optional<Merchant> findById(String merchantId);
    public List<MerchantProductPriceDTO> showPriceMerchantStock(String productId);
    public Integer getStocks(String merchantId,String productId);
    public Integer getPrice(String merchantId,String productId);


    }
