package com.example.emag.model.dto.user;

import com.example.emag.model.dto.order.OrderWithoutOwnerDTO;
import com.example.emag.model.entities.Order;
import com.example.emag.model.entities.Product;
import com.example.emag.model.entities.UserProductsInCart;
import lombok.Data;

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
    private String birthDate;
//    private List<Product> likedProducts;
//    private List<UserProductsInCart> productsInCart;
    private List<OrderWithoutOwnerDTO> pastOrders;
    //todo test if the produced and user products in cart works
}
