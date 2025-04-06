package com.scm20.helpers;

public class resourceNotFoundException extends RuntimeException{
   public resourceNotFoundException(String message){
    super(message);
   }

   public resourceNotFoundException(){
    super("Resource Not Found");
   }
}
