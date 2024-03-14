package com.portal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal.model.dto.DtoResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import static com.portal.security.TokenJwtConfig.*;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@GetMapping("/logout")
	public ResponseEntity<DtoResponse<?>> logout(@RequestHeader("Authorization") String token){
		DtoResponse<?> resp = new DtoResponse<>();

		SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
		String username = Jwts.parser().verifyWith(key).build().parseSignedClaims(token.replace(PREFIX_TOKEN, "")).getPayload().getSubject();

		WHITE_LIST.remove(username);
		resp.setCode_status(0);
		resp.setMessage("Tú sesión fue cerrada con exitoso.");

		return ResponseEntity.ok().body(resp);
	}
}
