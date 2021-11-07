package com.sgu.agency.dtos.response;

import com.sgu.agency.dal.entity.ImportingOrder;
import com.sgu.agency.dal.entity.SellingOrder;
import lombok.Data;

@Data
public class SellingTransactionDto {
    private String id;
    private ProductDetailDto productDetail;
    private Integer quantity;
    private Double price;
    private SellingOrder sellingOrder;
}
