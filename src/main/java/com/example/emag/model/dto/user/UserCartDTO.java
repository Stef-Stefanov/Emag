package com.example.emag.model.dto.user;

import com.example.emag.model.dto.cart.UserHasProductsInCardWithoutUserIdDTO;
import lombok.Data;

import java.util.List;
@Data
public class UserCartDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private List<UserHasProductsInCardWithoutUserIdDTO> productsInCart;
}
