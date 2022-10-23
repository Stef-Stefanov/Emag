package com.example.emag.service;

import com.example.emag.model.entities.*;
import com.example.emag.model.exceptions.BadRequestException;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService extends AbstractService{
    public double makeOrder(long uid) {
        User user = getUserById(uid);
        OrderProductKey pk = new OrderProductKey();
        Order order = new Order();
        OrderProduct orderProduct = new OrderProduct();
        order.setUserId(user.getId());
        order.setCreatedAt(LocalDateTime.now());
        double totalPrice = 0;
        if(user.getProductsInCart().size() == 0){
            throw new BadRequestException("Cart is empty");
        }
        else{
            List<UserProductsInCart> productList = user.getProductsInCart();
            System.out.println(productList.size());
            for (UserProductsInCart productsInCart : productList) {
                // get every product check if current quantity is enough for order
                // check price of every product if there is discount
                // add price of every product to total amount
                // clear cart - make order
                // update quantity of products
                // save order in history
                //todo fix return type to dto
                Product product = productsInCart.getProduct();
                System.out.println(product.getName());
                int quantityOfCurrentProductInCart = productsInCart.getQuantity();
                int totalQuantityInShop = product.getQuantity();
                if(quantityOfCurrentProductInCart > totalQuantityInShop){
                    throw new BadRequestException("Not enough quantity of " + product.getName() +
                            " product only " + product.getQuantity() + " has left");
                }
                product.setQuantity(totalQuantityInShop - quantityOfCurrentProductInCart);
                orderRepository.save(order);
                productRepository.save(product);
                orderProduct.setOrder(order);
                orderProduct.setProduct(product);
                orderProduct.setId(pk);
                pk.setOrderId(order.getId());
                pk.setProductId(product.getId());
                orderProduct.setQuantity(quantityOfCurrentProductInCart);
                orderProductRepository.save(orderProduct);
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
        order.setPrice(totalPrice);
        orderRepository.save(order);
        user.getProductsInCart().clear();
        return totalPrice;
    }
}
