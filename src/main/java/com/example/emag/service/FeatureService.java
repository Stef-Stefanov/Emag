package com.example.emag.service;

import com.example.emag.model.dto.FeatureDTO;
import com.example.emag.model.entities.Feature;
import com.example.emag.model.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class FeatureService extends AbstractService{

    public FeatureDTO add(FeatureDTO dto) {
        validate(dto);
        Feature feature = new Feature();
        feature.setName(dto.getName());
        System.out.println(feature.getName());
        return modelMapper.map(featureRepository.save(feature), FeatureDTO.class);
    }

    private void validate(FeatureDTO dto){
        if(dto.getName().strip().length() < 1 || dto.getName().length() > 255){
            throw new BadRequestException("Feature name size is not valid");
        }
    }

    public FeatureDTO delete(long fid) {
        Feature feature = getFeatureById(fid);
        FeatureDTO dto = modelMapper.map(feature, FeatureDTO.class);
        featureRepository.deleteById(fid);
        return dto;
    }
}
