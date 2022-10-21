package com.example.emag.controller;

import com.example.emag.model.dto.FeatureDTO;
import com.example.emag.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FeatureController extends AbstractController {

    @Autowired
    private FeatureService featureService;
    @PostMapping("/features")
    public FeatureDTO addFeature(@RequestBody FeatureDTO dto){
        //todo check if admin
        return featureService.add(dto);
    }

    @DeleteMapping("features/{fid}")
    public FeatureDTO deleteFeature(@PathVariable int fid){
        //todo check if admin
        return featureService.delete(fid);
    }
}
