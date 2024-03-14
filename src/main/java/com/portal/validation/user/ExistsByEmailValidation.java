package com.portal.validation.user;

import org.springframework.beans.factory.annotation.Autowired;

import com.portal.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistsByEmailValidation implements ConstraintValidator<ExistsByEmail, String> {

	@Autowired
	private UserService userServ;

	@Override
	public boolean isValid(String correo, ConstraintValidatorContext context) {
		if(userServ == null) {
			return true;
		}
		return !userServ.existsByEmail(correo);
	}

}
