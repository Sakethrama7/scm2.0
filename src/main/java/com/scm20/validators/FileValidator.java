package com.scm20.validators;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.*;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile,MultipartFile>{

    private static final long MAX_FILE_SIZE= 1024 * 1024 * 2 ;

    // type

    // height

    // width

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        
        if(file == null || file.isEmpty()){
           // context.disableDefaultConstraintViolation();
           // context.buildConstraintViolationWithTemplate("File cannot be empty").addConstraintViolation();

            return true;
        }
        // file size
        if(file.getSize() > MAX_FILE_SIZE){
             context.disableDefaultConstraintViolation();
             context.buildConstraintViolationWithTemplate("File size should be less than 2MB").addConstraintViolation();

             return false;
        }

        //file resolution
        // try {
        //     BufferedImage bufferedImage=ImageIO.read(file.getInputStream());
            
        //     if(bufferedImage.getHeight() > 1080){

        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        return true;
        
    }

   

}
