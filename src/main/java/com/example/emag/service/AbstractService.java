package com.example.emag.service;

import com.example.emag.model.dao.ProductDAO;
import com.example.emag.model.dto.product.ProductAddDTO;
import com.example.emag.model.entities.*;
import com.example.emag.model.exceptions.NotFoundException;
import com.example.emag.model.repositories.*;
import com.example.emag.model.util.EmailSender;
import org.aspectj.weaver.ast.Not;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.Random;

@Service
public abstract class AbstractService {
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected ModelMapper modelMapper;
    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected UserCartRepository userCartRepository;
    @Autowired
    protected ProductImageRepository productImageRepository;
    @Autowired
    protected FeatureRepository featureRepository;
    @Autowired
    protected ProductFeatureRepository productFeatureRepository;
    @Autowired
    protected DiscountRepository discountRepository;
    @Autowired
    protected ReviewRepository reviewRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected OrderProductRepository orderProductRepository;
    @Autowired
    protected ProductDAO productDAO;
    @Autowired
    protected EmailSender emailSender;
    protected static String adminPassword = "123";

    /**
     *
     * The changeAdminPasswordCronJob changes the master admin password every so often to a new randomly generated one.
     *
     * The master adminPassword is used to make a regular user into an administrator.
     * Once the application is run there is a small window of opportunity where the client can become an administrator
     * using the initial admin password.
     * After that the CronJob changes that password to a new randomly generated password. //todo change line if implement true random
     * In oder for a new user to be made into an administrator, a already existing admin needs to look up the
     * adminPassword using the secure method lookUpAdminPassword and give the current password
     */
    @Async
    @Scheduled(cron ="1 * * * * *",zone = "EET")
    protected void changeAdminPasswordCronJob(){
        // todo implement https://sourceforge.net/projects/trng-random-org/
        adminPassword = String.valueOf(new Random().nextLong());
        System.out.println("working cron job");
        System.out.println(adminPassword);
    }

//    protected Product findProductById(long id) {
//        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
//    }

    protected Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    protected User getUserById(long id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    protected Product getProductById(long pid){
        return productRepository.findById(pid).orElseThrow(() -> new NotFoundException("Product not found"));
    }

    protected Feature getFeatureById(long id){
        return featureRepository.findById(id).orElseThrow(() -> new NotFoundException("Feature not found"));
    }

    protected Discount getDiscountById(long id){
        return discountRepository.findById(id).orElseThrow(() -> new NotFoundException("Discount not found"));
    }

    protected Review getReviewById(long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new NotFoundException("Review not found"));
    }

    protected ProductImage getProductImageById(long id){
        return productImageRepository.findById(id).orElseThrow(() -> new NotFoundException("Product image not found"));
    }

    protected void sendEmail(Product product){
        List<User> subscribers = product.getLikes();
        for (User user : subscribers){
            emailSender.sendMessage(user.getEmail(), product);
        }
    }


}
