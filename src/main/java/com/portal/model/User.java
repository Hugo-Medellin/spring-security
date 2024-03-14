package com.portal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;

	@Column(unique = true)
	private String username;
	
	@Column(unique = true)
	private String correo;

	@NotBlank
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Solo se mostrará la contraseña cuando se guarde un nuevo usuario
	private String password;

	@ManyToMany
	@JoinTable(
			name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"),
				uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "rol_id"}) }
			)
	private List<Rol> roles;
	private boolean enabled;

	@PrePersist
	public void prePersist() {
		enabled = true;
	}
}