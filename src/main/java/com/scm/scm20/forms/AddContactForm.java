package com.scm.scm20.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.scm20.validator.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddContactForm {
    
    @NotBlank(message = "Name can not be empty")
    private String name;
    @NotBlank(message = "Email can not be empty")
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Phone number can not be empty")
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits")
    private String phoneNumber;
    private String Address;
    private String about;
    private String websiteLink;
    private String linkedInLink;
    private boolean favorite;

    // annotation create karenge that will validate image file(size, resolution)
    @ValidFile
    private MultipartFile proFilePicture; 

    private String pictureUrl;
}
