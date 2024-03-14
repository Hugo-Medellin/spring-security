package com.portal.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.portal.validation.user.ExistsByEmail;
import com.portal.validation.user.ExistsByUsername;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {

	@NotBlank
	@Size(min = 4, max = 15)
	@ExistsByUsername
	private String username;

	@NotBlank
	@Email
	@ExistsByEmail
	private String correo;

	@NotBlank
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Solo se mostrará la contraseña cuando se guarde un nuevo usuario
	@Size(min = 8, message = "debe contener mínimo 8 caracteres.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[a-zA-Z\\d@#$%^&+=!]).{8,}$", 
	 message = "no cumple con los requisitos")
	private String password;
	private boolean admin;
}