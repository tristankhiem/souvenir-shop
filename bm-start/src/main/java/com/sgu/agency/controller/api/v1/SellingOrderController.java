package com.sgu.agency.controller.api.v1;


import com.sgu.agency.biz.services.impl.SellingOrderServiceImpl;
import com.sgu.agency.common.utils.BCryptHelper;
import com.sgu.agency.configuration.security.jwt.JwtProvider;
import com.sgu.agency.dtos.request.BaseSearchDto;
import com.sgu.agency.dtos.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/selling-order")
public class SellingOrderController extends BaseController {
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    SellingOrderServiceImpl sellingOderService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    public SellingOrderController() {

    }
    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody BaseSearchDto<List<SellingOrderDto>> search) {
        BaseSearchDto<List<SellingOrderDto>> result = sellingOderService.findAll(search);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), result));
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody SellingOrderFullDto sellingOrderFullDto) {
        List<String> errMessages = validateInsert(sellingOrderFullDto);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }

        SellingOrderFullDto sellingOrderFull = sellingOderService.insert(sellingOrderFullDto);


        ResponseEntity<?> res = sellingOrderFull != null  ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu hóa đơn xuất bán thành công"), HttpStatus.OK.value(), sellingOrderFull))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu hóa đơn xuất bán"),  HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody SellingOrderFullDto sellingOrderFullDto) {
        List<String> errMessages = validateInsert(sellingOrderFullDto);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }

        SellingOrderFullDto sellingOrderFull = sellingOderService.update(sellingOrderFullDto);


        ResponseEntity<?> res = sellingOrderFull != null  ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Lưu hóa đơn xuất bán thành công"), HttpStatus.OK.value(), sellingOrderFull))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi lưu hóa đơn xuất bán"),  HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }

    private List<String> validateInsert(SellingOrderFullDto sellingOrderFullDto) {
        List<String> result = new ArrayList<>();
        sellingOrderFullDto.setAddress(BCryptHelper.encrypt(sellingOrderFullDto.getAddress()));
        sellingOrderFullDto.setReceivePerson(BCryptHelper.encrypt(sellingOrderFullDto.getReceivePerson()));

        return result;
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        List<String> errMessages = validateDelete(id);
        if(errMessages.size() > 0) {
            return ResponseEntity.ok(new ResponseDto(errMessages, HttpStatus.BAD_REQUEST.value(), ""));
        }
        boolean result = sellingOderService.delete(id);

        ResponseEntity<?> res = result ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Xóa hóa đơn xuất bán thành công"), HttpStatus.OK.value(), result))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi xóa hóa đơn xuất bán"), HttpStatus.BAD_REQUEST.value(), ""));
        return res;
    }
    private List<String> validateDelete(String id) {
        List<String> result = new ArrayList<>();

        if (id.isEmpty()){
            result.add("Quyền không tồn tại");
        }

        return result;
    }

    @GetMapping("/get-by-customer-id/{id}")
    public ResponseEntity<?> getByCustomerId(@PathVariable String id) {
        List<SellingOrderFullDto> sellingOrderFullDto = sellingOderService.getByCustomerId(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), sellingOrderFullDto));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getSellingOrderFullById(@PathVariable String id) {
        SellingOrderFullDto sellingOrderFullDto = sellingOderService.getSellingOrderFullById(id);
        return ResponseEntity.ok(new ResponseDto(Arrays.asList("Lấy dữ liệu thành công"), HttpStatus.OK.value(), sellingOrderFullDto));
    }

    @PostMapping("/getMonthRevenue")
    public ResponseEntity<?> getMonthRevenue(@RequestBody RangeDateDto rangeDateDto) {
        if(rangeDateDto.getFromDate() > rangeDateDto.getToDate()){
            return ResponseEntity.ok(new ResponseDto(Arrays.asList("Ngày bắt đầu phải nhỏ hơn ngày kết thúc"), HttpStatus.BAD_REQUEST.value(), ""));
        }
        List<MonthRevenueDetailDto> monthRevenueDtos = sellingOderService.getMonthRevenue(rangeDateDto);

        ResponseEntity<?> res = monthRevenueDtos != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Doanh thu"), HttpStatus.OK.value(), monthRevenueDtos))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi thống kê doanh thu"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @PostMapping("/getDateRevenue")
    public ResponseEntity<?> getDateRevenue(@RequestBody RangeDateDto rangeDateDto) {
        if(rangeDateDto.getFromDate() > rangeDateDto.getToDate()){
            return ResponseEntity.ok(new ResponseDto(Arrays.asList("Ngày bắt đầu phải nhỏ hơn ngày kết thúc"), HttpStatus.BAD_REQUEST.value(), ""));
        }
        List<DateRevenueDetailDto> dateRevenueDtos = sellingOderService.getDateRevenue(rangeDateDto);

        ResponseEntity<?> res = dateRevenueDtos != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Doanh thu"), HttpStatus.OK.value(), dateRevenueDtos))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi thống kê doanh thu"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }

    @PostMapping("/getYearRevenue")
    public ResponseEntity<?> getYearRevenue(@RequestBody RangeDateDto rangeDateDto) {

        if(rangeDateDto.getFromDate() > rangeDateDto.getToDate()){
            return ResponseEntity.ok(new ResponseDto(Arrays.asList("Ngày bắt đầu phải nhỏ hơn ngày kết thúc"), HttpStatus.BAD_REQUEST.value(), ""));
        }
        List<YearRevenueDetailDto> yearRevenueDtos = sellingOderService.getYearRevenue(rangeDateDto);

        ResponseEntity<?> res = yearRevenueDtos != null ? ResponseEntity.ok(new ResponseDto(Arrays.asList("Doanh thu"), HttpStatus.OK.value(), yearRevenueDtos))
                : ResponseEntity.ok(new ResponseDto(Arrays.asList("Lỗi thống kê doanh thu"), HttpStatus.BAD_REQUEST.value(), ""));

        return res;
    }
}
