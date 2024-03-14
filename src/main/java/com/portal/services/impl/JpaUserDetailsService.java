package com.portal.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.model.User;
import com.portal.repository.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(JpaUserDetailsService.class);

	@Autowired
	private UserRepository userRepo;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOG.info("loadUserByUsername");

		Optional<User> userOptional = userRepo.findByUsername(username);

		if(userOptional.isEmpty()) {
			throw new UsernameNotFoundException(String.format("El usuario: %s no existe en el sistema.", username));
		}

		User user = userOptional.orElseThrow();
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(
				user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
	}

}
