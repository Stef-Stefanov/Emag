package com.example.emag.model.dto.user;

import com.example.emag.model.dto.cart.UserHasProductsInCardWithoutUserIdDTO;
import com.example.emag.model.dto.order.OrderWithoutOwnerDTO;
import com.example.emag.model.dto.product.ProductDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserWithoutPassDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String createdAt;
    private int isSubscribed;
    private int isAdmin;
    private String phoneNumber;
    private LocalDate birthDate;
    private List<ProductDTO> likedProducts;
    private List<UserHasProductsInCardWithoutUserIdDTO> productsInCart;
    private List<OrderWithoutOwnerDTO> pastOrders;
}
