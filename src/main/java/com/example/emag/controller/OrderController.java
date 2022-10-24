package com.example.emag.controller;

import com.example.emag.model.dto.order.MadeOrderDTO;
import com.example.emag.model.entities.Order;
import com.example.emag.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController extends AbstractController{
    @Autowired
    private OrderService orderService;

    @PostMapping("orders/{uid}")
    public MadeOrderDTO makeOrder(@PathVariable long uid){
        return orderService.makeOrder(uid);
    }
}
