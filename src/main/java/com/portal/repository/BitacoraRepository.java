package com.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.model.Bitacora;

@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Integer> {

}
