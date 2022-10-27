package com.example.emag.service;

import com.example.emag.model.dto.discount.DiscountRequestDTO;
import com.example.emag.model.dto.discount.DiscountResponseDTO;
import com.example.emag.model.entities.Discount;
import com.example.emag.model.entities.Product;
import com.example.emag.model.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscountService extends AbstractService{
    public DiscountResponseDTO add(DiscountRequestDTO requestDTO) {
        validateDTO(requestDTO);
        Discount discount = new Discount();
        discount.setDiscountPercentage(requestDTO.getDiscountPercentage());
        discount.setStartDate(requestDTO.getStartDate());
        discount.setExpireDate(requestDTO.getExpireDate());
        return modelMapper.map(discountRepository.save(discount), DiscountResponseDTO.class);
    }

    private void validateDTO(DiscountRequestDTO requestDTO) {
        if(requestDTO.getDiscountPercentage() > 99 || requestDTO.getDiscountPercentage() < 1){
            throw new BadRequestException("Invalid discount percentage given");
        }
        if(requestDTO.getStartDate().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Discount can't be started in the past");
        }
        if(requestDTO.getExpireDate().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Discount is already expired without even started, fix your expire date");
        }
    }

    @Transactional
    public DiscountResponseDTO remove(long id) {
        Discount discount = getDiscountById(id);
        DiscountResponseDTO dto = new DiscountResponseDTO();
        checkProductsIfHasThisDiscount(discount);
        modelMapper.map(discount,dto);
        discountRepository.deleteById(id);
        return dto;
    }

    private void checkProductsIfHasThisDiscount(Discount discount) {
        List<Product> products = productRepository.findAllByDiscount(discount);
        if(products.isEmpty()){
            return;
        }
        for(Product product : products){
            product.setDiscount(null);
            productRepository.save(product);
        }
    }
}
