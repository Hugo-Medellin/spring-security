package com.portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.model.Rol;

@Repository
public interface RoleRepository extends JpaRepository<Rol, Long> {
	Optional<Rol> findByName(String name);
}
