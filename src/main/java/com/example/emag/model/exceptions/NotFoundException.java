package com.example.emag.model.exceptions;


public class NotFoundException extends RuntimeException{

   public NotFoundException(String text){
       super(text);
   }
}
