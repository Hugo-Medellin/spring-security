package com.portal.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.model.Rol;
import com.portal.model.User;
import com.portal.model.dto.RegisterUser;
import com.portal.repository.RoleRepository;
import com.portal.repository.UserRepository;
import com.portal.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return (List<User>) userRepo.findAll();
	}

	@Override
	public Optional<User> findUserById(Long user_id) {
		return userRepo.findById(user_id);
	}

	@Override
	@Transactional
	public User save(RegisterUser regUser) {

		Optional<Rol> optionalRoleUser = roleRepo.findByName("ROLE_USER");
		List<Rol> roles = new ArrayList<>();

		optionalRoleUser.ifPresent(roles::add);

		if(regUser.isAdmin()) {
			Optional<Rol> optionalRoleAdmin = roleRepo.findByName("ROLE_ADMIN");
			optionalRoleAdmin.ifPresent(roles::add);
		}

		User user = new User();
		user.setUsername(regUser.getUsername());
		user.setCorreo(regUser.getCorreo());
		user.setRoles(roles);
		user.setEnabled(true);

		user.setPassword(passwordEncoder.encode(regUser.getPassword()));

		return userRepo.save(user);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepo.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String correo) {
		return userRepo.existsByCorreo(correo);
	}

}
