package com.portal.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoResponse<T> {

	private Integer code_status;
	private String message;
	private T dato;
	private List<T> datos;
}
