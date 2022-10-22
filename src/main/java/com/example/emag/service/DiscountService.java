package com.example.emag.service;

import com.example.emag.model.dto.discount.DiscountRequestDTO;
import com.example.emag.model.dto.discount.DiscountResponseDTO;
import com.example.emag.model.entities.Discount;
import com.example.emag.model.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public DiscountResponseDTO remove(long id) {
        Discount discount = getDiscountById(id);
        DiscountResponseDTO dto = new DiscountResponseDTO();
        modelMapper.map(discount,dto);
        discountRepository.deleteById(id);
        return dto;
    }
}
