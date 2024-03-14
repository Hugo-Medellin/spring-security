package com.portal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "persona")
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long persona_id;

	@NotBlank
	private String nombres;

	@NotBlank
	private String ap_paterno;

	@NotBlank
	private String ap_materno;

	@NotBlank
	private String fecha_nacimiento;
	private String curp;
	private String rfc;
	private String telefono;

	@NotBlank
	private String nacionalidad;
	private String foto;

	@NotBlank
	private boolean enabled;
}