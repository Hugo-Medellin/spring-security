package com.portal.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.model.User;
import com.portal.model.dto.AuthenticationResponse;
import com.portal.model.dto.DtoResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.portal.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private AuthenticationManager authManager;
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		LOG.info("attemptAuthentication");
		User user = null;
		String username = null;
		String password = null;

		try {
			user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			username = user.getUsername();
			password = user.getPassword();
		} catch (StreamReadException e) {
			e.printStackTrace();
		} catch (DatabindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		return authManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		LOG.info("successfulAuthentication");
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
		String existsUserLogged = WHITE_LIST.get(user.getUsername());

		if(existsUserLogged != null) {
			DtoResponse<String> resp = new DtoResponse<>();
			resp.setCode_status(1);
			resp.setMessage("Tienes una sesión activa. Cierrala antes de iniciar una nueva.");

			response.getWriter().write(new ObjectMapper().writeValueAsString(resp));
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(CONTENT_TYPE);
			return;
		}

		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

		Claims claims = Jwts.claims().add("authorities", new ObjectMapper().writeValueAsString(roles)).build();

		byte[] secretBytes = SECRET_KEY.getBytes();
		SecretKey key = Keys.hmacShaKeyFor(secretBytes);

		String token = Jwts.builder()
				.expiration(new Date(System.currentTimeMillis() + TOKEN_LIFETIME))
				.issuedAt(new Date())
				.claims(claims)
				.subject(user.getUsername())
				.signWith(key)
				.compact();
		response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

		DtoResponse<AuthenticationResponse> dtoResp = new DtoResponse<>();
		AuthenticationResponse authUser = new AuthenticationResponse();
		authUser.setUsername(user.getUsername());
		authUser.setToken(token);

		dtoResp.setCode_status(0);
		dtoResp.setMessage(String.format("Bienvenido %s. Inicio de sesión con éxito.", user.getUsername()));
		dtoResp.setDato(authUser);

		WHITE_LIST.put(user.getUsername(), token);

		response.getWriter().write(new ObjectMapper().writeValueAsString(dtoResp));
		response.setContentType(CONTENT_TYPE);
		response.setStatus(200);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		LOG.info("unsuccessfulAuthentication");
		DtoResponse<String> dtoResp = new DtoResponse<>();
		dtoResp.setCode_status(1);
		dtoResp.setMessage("Error al autenticar. Usuario o contraseña incorrectos.");

		response.getWriter().write(new ObjectMapper().writeValueAsString(dtoResp));
		response.setStatus(401);
		response.setContentType(CONTENT_TYPE);
	}
}