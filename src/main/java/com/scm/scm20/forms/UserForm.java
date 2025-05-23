package com.scm.scm20.forms;

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
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder 

public class UserForm {

    @NotBlank(message = "username is required")
    @Size(min = 2, message = "min 3 character is required")
    private String name;
    @Email(message = "Invalid Email Address")
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "password is required")
    @Size(min = 6, message = "min 6 character is required")
    private String password;
    @NotBlank(message = "about is required")
    private String about;
    @Size(min = 8, max = 12, message = "Invalid phone number")
    private String phoneNumber;
}
