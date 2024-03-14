package com.portal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal.model.User;
import com.portal.model.dto.DtoResponse;
import com.portal.model.dto.RegisterUser;
import com.portal.services.UserService;
import com.portal.utils.ValidationErrors;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@PostMapping
	public ResponseEntity<DtoResponse<String>> create(@Valid @RequestBody RegisterUser user, BindingResult result){
		LOG.info("Create into.");
		DtoResponse<String> resp = new DtoResponse<>();

		if(result.hasFieldErrors()) {
			resp.setCode_status(1);
			resp.setMessage(ValidationErrors.validation(result).toString());
			return ResponseEntity.badRequest().body(resp);
		}

		User user_valid = userService.save(user);
		if(user_valid != null) {
			resp.setCode_status(0);
			resp.setMessage("Tu registro fue guardado con éxito.");
		} else {
			resp.setCode_status(1);
			resp.setMessage("Ocurrió un error al guardar el registro. Intenta más tarde.");
		}

		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}

	@PostMapping("/register")
	public ResponseEntity<DtoResponse<String>> register(@Valid @RequestBody RegisterUser user, BindingResult result) {
		LOG.info("register into.");
		user.setAdmin(false);
		return create(user, result);
	}

	@GetMapping("/list")
	public ResponseEntity<DtoResponse<User>> listUsers(){
		LOG.info("listUsers into");
		DtoResponse<User> dtoResp = new DtoResponse<>();

		dtoResp.setCode_status(0);
		dtoResp.setDatos(userService.findAll());
		return ResponseEntity.ok().body(dtoResp);
	}
}
