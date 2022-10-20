package com.example.emag.controller;

import com.example.emag.model.exceptions.BadRequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class FileController extends AbstractController{

    @GetMapping("/images/{filePath}")
    public void getImage(@PathVariable String filePath, HttpServletResponse resp){
        File f = new File("uploads" + File.separator + filePath);
        if(!f.exists()){
            throw new BadRequestException("File does not exists");
        }
        try {
            Files.copy(f.toPath(), resp.getOutputStream());
        } catch (IOException e) {
            throw new BadRequestException("Something happened");
        }
    }
}
