package com.portal.services;

import java.util.Optional;

import com.portal.model.Bitacora;

public interface BitacoraService {
	Optional<Bitacora> findById(Integer bitacora_id);
	void save(Bitacora bitacora);
}
