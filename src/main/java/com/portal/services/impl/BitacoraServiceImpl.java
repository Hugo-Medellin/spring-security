package com.portal.services.impl;
 
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.portal.model.Bitacora;
import com.portal.repository.BitacoraRepository;
import com.portal.services.BitacoraService;

@Service
public class BitacoraServiceImpl implements BitacoraService {

	private BitacoraRepository bitacoraRepo;

	public BitacoraServiceImpl(BitacoraRepository bitacoraRepository) {
		this.bitacoraRepo = bitacoraRepository;
	}

	@Override
	public Optional<Bitacora> findById(Integer bitacora_id) {
		return bitacoraRepo.findById(bitacora_id);
	}

	@Override
	public void save(Bitacora bitacora) {
		bitacoraRepo.save(bitacora);
	}

}
