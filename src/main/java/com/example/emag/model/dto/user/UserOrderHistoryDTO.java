package com.example.emag.model.dto.user;

import com.example.emag.model.dto.order.OrderWithoutOwnerDTO;
import lombok.Data;

import java.util.List;
@Data
public class UserOrderHistoryDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private List<OrderWithoutOwnerDTO> pastOrders;
}
