package com.example.emag.model.util;

import com.example.emag.model.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    @Autowired
    private JavaMailSender javaEmailSender;

    public void sendMessage(String receiver, Product product) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(receiver);
        email.setSubject("Favourite product on discount");
        email.setText("Your favourite products " + product.getName() + " is now discounted with " +
                product.getDiscount().getDiscountPercentage() +" percent. Current price is now " +
                (product.getRegularPrice()-(product.getRegularPrice() *
                        ((double)product.getDiscount().getDiscountPercentage()/100)))
        + ". Limited offer until " + product.getDiscount().getExpireDate());
        javaEmailSender.send(email);
    }
}
