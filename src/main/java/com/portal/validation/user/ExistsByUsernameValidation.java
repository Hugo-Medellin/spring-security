package com.portal.validation.user;

import org.springframework.beans.factory.annotation.Autowired;

import com.portal.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

	@Autowired
	private UserService userServ;

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		if(userServ == null) {
			return true;
		}
		return !userServ.existsByUsername(username);
	}

}
