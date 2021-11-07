package com.sgu.agency.dtos.response;

import com.sgu.agency.common.enums.OrderStatusEnum;
import com.sgu.agency.dal.entity.ImportingOrder;
import lombok.Data;

import java.util.Date;

@Data
public class ImportingTransactionDto {
    private String id;
    private ProductDetailDto productDetail;
    private Integer quantity;
    private Double price;
    private ImportingOrder importingOrder;
}
