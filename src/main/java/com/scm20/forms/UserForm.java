package com.scm20.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {
    // This class should contain the fields that are present in the form . Because this class is to recieve the data that we enter in the form.
    // To valiate the form you need to add these validation annotations
    @NotBlank(message = "Username is Required")
    @Size(min=3,message="Min 3 Characters is Required")
    private String name;

    @Email(message = "Invalid Email Address")
    @NotBlank(message = "Email is Required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=6,message="Min 6 Characters is required")
    private String password;

    @NotBlank(message = "About is required")
    private String about;

    @Size(min=8,max=12,message = "Invalid Phone Number")
    private String phoneNumber;
}
