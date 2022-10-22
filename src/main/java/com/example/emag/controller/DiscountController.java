package com.example.emag.controller;

import com.example.emag.model.dto.discount.DiscountRequestDTO;
import com.example.emag.model.dto.discount.DiscountResponseDTO;
import com.example.emag.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DiscountController extends AbstractController {
    @Autowired
    private DiscountService discountService;
    @PostMapping("discounts")
    public DiscountResponseDTO addDiscount(@RequestBody DiscountRequestDTO requestDTO){
        //todo check if admin
        return discountService.add(requestDTO);
    }

    @DeleteMapping("discounts/{id}")
    public DiscountResponseDTO removeDiscount(@PathVariable long id){
        //todo check if admin
        return discountService.remove(id);
    }
}
