package com.example.emag.controller;

import com.example.emag.model.dto.order.MadeOrderDTO;
import com.example.emag.model.dto.user.UserOrderHistoryDTO;
import com.example.emag.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderController extends AbstractController{
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public MadeOrderDTO makeOrder(HttpServletRequest req){
        long uid = getLoggedUserId(req);
        return orderService.makeOrder(uid);
    }
    @GetMapping("/users/history")
    public UserOrderHistoryDTO lookUpUserOrderHistory(HttpServletRequest req){
        long uid = getLoggedUserId(req);
        return orderService.getOrderHistory(uid);
    }
}
