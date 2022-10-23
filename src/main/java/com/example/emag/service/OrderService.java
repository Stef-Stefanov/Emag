package com.example.emag.service;

import com.example.emag.model.entities.*;
import com.example.emag.model.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService extends AbstractService{
    public double makeOrder(long uid) {
        User user = getUserById(uid);
        double totalPrice = 0;
        if(user.getProductsInCart().size() == 0){
            throw new BadRequestException("Cart is empty");
        }
        else{
            List<UserProductsInCart> productList = user.getProductsInCart();
            for (UserProductsInCart productsInCart : productList) {
                // get every product check if current quantity is enough for order
                // check price of every product if there is discount
                // add price of every product to total amount
                // clear cart - make order
                // update quantity of products
                // save order in history todo
                Product product = productsInCart.getProduct();
                int quantityOfCurrentProductInCart = productsInCart.getQuantity();
                int totalQuantityInShop = product.getQuantity();
                if(quantityOfCurrentProductInCart > totalQuantityInShop){
                    throw new BadRequestException("Not enough quantity of " + product.getName() +
                            " product only " + product.getQuantity() + " has left");
                }
                product.setQuantity(totalQuantityInShop - quantityOfCurrentProductInCart);
                productRepository.save(product);
                userCartRepository.delete(productsInCart);
                double regularPrice = product.getRegularPrice();
                Discount discount = product.getDiscount();
                if(discount != null ){
                    double percentageDiscount = discount.getDiscountPercentage();
                    double discountNumber = percentageDiscount / 100;
                    System.out.println(discountNumber);
                    double discountedPrice = (1 - discountNumber)* regularPrice;
                    System.out.println("Discount price is " +discountedPrice);
                    System.out.println("total price is " + totalPrice);
                    totalPrice += discountedPrice * quantityOfCurrentProductInCart;
                }
                else{
                    totalPrice += regularPrice * quantityOfCurrentProductInCart;
                    System.out.println("total price in else is " + totalPrice);
                }
            }
        }
        user.getProductsInCart().clear();
        return totalPrice;
    }
}
