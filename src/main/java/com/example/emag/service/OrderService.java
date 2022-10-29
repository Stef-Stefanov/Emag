package com.example.emag.service;

import com.example.emag.model.dto.order.MadeOrderDTO;
import com.example.emag.model.dto.order.ProductOrderDTO;
import com.example.emag.model.dto.user.UserOrderHistoryDTO;
import com.example.emag.model.dto.user.UserWithoutPassDTO;
import com.example.emag.model.entities.*;
import com.example.emag.model.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService extends AbstractService{
    @Transactional 
    public MadeOrderDTO makeOrder(long uid) {
        User user = getUserById(uid);
        Order order = new Order();
        double totalPrice = 0;
        if(user.getProductsInCart().size() == 0){
            throw new BadRequestException("Cart is empty");
        }
        List<UserProductsInCart> productsInCartList = user.getProductsInCart();
        scanProductsInCartOneByOne(productsInCartList, order, totalPrice, user);
        orderRepository.save(order);
        return setMadeOrderDTO(user, order, productsInCartList);
    }

    private MadeOrderDTO setMadeOrderDTO(User user, Order order, List<UserProductsInCart> productsInCartList) {
        MadeOrderDTO dto = new MadeOrderDTO();
        dto.setFirstName(user.getFirstName());
        dto.setTotalPrice(order.getPrice());
        dto.setProducts(productsInCartList.stream().map(productsInCart ->
                modelMapper.map(productsInCart.getProduct(), ProductOrderDTO.class)).collect(Collectors.toList()));
        // мапваме имената на продуктите към дтото, но също мапваме и количеството, което е останало, а ние не искаме това
        List<ProductOrderDTO> orderDTOList = dto.getProducts();
        for (ProductOrderDTO entity : orderDTOList){
            for(UserProductsInCart productsInCart : productsInCartList){
                if(productsInCart.getProduct().getName().equals(entity.getName())){
                    entity.setQuantity(productsInCart.getQuantity());
                    // сетваме количеството на това, което е поръчано
                }
            }
        }
        return dto;
    }

    private void scanProductsInCartOneByOne(List<UserProductsInCart> productList, Order order,
                                            double totalPrice, User user){

        for (UserProductsInCart currentProductInCart : productList) {
            Product product = currentProductInCart.getProduct();
            int leftQuantityInShop = checkIfQuantityIsEnough(product, currentProductInCart);
            product.setQuantity(leftQuantityInShop);// сетваме новото количество
            order.setPrice(order.getPrice() + sumTotalPrice(currentProductInCart, totalPrice));
            order.setUser(user);
            order.setCreatedAt(LocalDateTime.now());
            orderProductsOneByOne(product, currentProductInCart, order);
        }
    }

    private void orderProductsOneByOne(Product product, UserProductsInCart currentProductInCart,Order order) {
        orderRepository.save(order); //даваме id на order
        productRepository.save(product); // записваме продукта с новото количество в базата
        OrderProduct orderProduct = new OrderProduct();// създаваме orderProduct с композитен ключ orderProductKey,
        // което е един запис от таблицата orders_have_products
        orderProduct.setOrder(order);// order_id
        orderProduct.setProduct(product); // product_id
        OrderProductKey pk = new OrderProductKey(); // прави се ключ така работи много към много с допълнителна колона
        pk.setOrderId(order.getId());// order_id
        pk.setProductId(product.getId());//product_id заедно са композитния ключ
        orderProduct.setId(pk);// сетва се композитния ключ
        orderProduct.setQuantity(currentProductInCart.getQuantity());// сетва се количеството поръчан продукт
        orderProductRepository.save(orderProduct);// запазва се поръчкатата в базата данни
        userCartRepository.delete(currentProductInCart);
    }

    private int checkIfQuantityIsEnough(Product product, UserProductsInCart currentProduct) {
        int quantityOfCurrentProductInCart = currentProduct.getQuantity();
        int totalQuantityInShop = product.getQuantity();
        if (quantityOfCurrentProductInCart > totalQuantityInShop) {
            throw new BadRequestException("Not enough quantity of " + product.getName() +
                    " product only " + product.getQuantity() + " has left");
        }
        return totalQuantityInShop - quantityOfCurrentProductInCart;
    }

    private double sumTotalPrice(UserProductsInCart currentProductInCart, double totalPrice){
        Product product = currentProductInCart.getProduct();
        Discount discount = product.getDiscount();
        int quantity = currentProductInCart.getQuantity();
        double regularPrice = product.getRegularPrice();

        if(discount != null ){
            double percentageDiscount = discount.getDiscountPercentage();
            double discountNumber = percentageDiscount / 100; // 20 / 100 -> 0.2
            double discountedPrice = (1 - discountNumber) * regularPrice;
            return totalPrice + (discountedPrice * quantity);
        }
        else{
            return totalPrice + (regularPrice * quantity);
        }
    }

    public UserOrderHistoryDTO getOrderHistory(long userId) {
        UserWithoutPassDTO dto = modelMapper.map(getUserById(userId), UserWithoutPassDTO.class);
        return modelMapper.map(dto,UserOrderHistoryDTO.class);
    }
}
