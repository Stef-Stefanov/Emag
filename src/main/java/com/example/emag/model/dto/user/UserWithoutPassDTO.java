package com.example.emag.model.dto.user;

import com.example.emag.model.dto.cart.CartWithProductWithQuantityDTO;
import com.example.emag.model.dto.order.OrderWithoutOwnerDTO;
import com.example.emag.model.dto.product.ProductDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty
    private boolean isSubscribed;
    @JsonProperty
    private boolean isAdmin;
    private String phoneNumber;
    private LocalDate birthDate;
    private List<ProductDTO> likedProducts;
    private List<CartWithProductWithQuantityDTO> productsInCart;
    private List<OrderWithoutOwnerDTO> pastOrders;
}
