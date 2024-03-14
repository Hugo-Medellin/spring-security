package com.portal.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;

public class ValidationErrors {

	public static List<String> validation(BindingResult result) {
		List<String> errors = new ArrayList<>();

		result.getFieldErrors().forEach( err -> {
			errors.add("El campo " + err.getField() + " " + err.getDefaultMessage());
		});
		return errors;
	}
}