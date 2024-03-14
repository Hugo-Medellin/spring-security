package com.portal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "bitacora_jwts")
public class Bitacora {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bitacora_id;
	private String token;
	private boolean enabled;
	
	@PrePersist
	public void prePersist() {
		enabled = true;
	}

	public Bitacora(String token, boolean enabled) {
		super();
		this.token = token;
		this.enabled = enabled;
	}
}