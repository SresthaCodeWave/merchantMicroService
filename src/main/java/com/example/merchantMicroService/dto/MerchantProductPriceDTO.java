package com.example.merchantMicroService.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MerchantProductPriceDTO {
    private String merchantId;
    private Integer productPrice;
    private Integer productStock;
}
