package com.portal.services;

import java.util.List;
import java.util.Optional;

import com.portal.model.User;
import com.portal.model.dto.RegisterUser;

public interface UserService {
	List<User> findAll();

	Optional<User> findUserById(Long user_id);
	User save(RegisterUser user);

	boolean existsByUsername(String username);
	boolean existsByEmail(String correo);
}
