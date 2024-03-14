package com.portal.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.model.dto.DtoResponse;
import com.portal.security.SimpleGrantedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.portal.security.TokenJwtConfig.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtValidationFilter.class);

	public JwtValidationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		LOG.info("doFilterInternal");
		String header = request.getHeader(HEADER_AUTHORIZATION);

		if(header == null || !header.startsWith(PREFIX_TOKEN)){
			chain.doFilter(request, response);
			return;
		}

		String token = header.replace(PREFIX_TOKEN, "");

		try {
			byte[] secret_bytes = SECRET_KEY.getBytes();
			SecretKey key = Keys.hmacShaKeyFor(secret_bytes);

			Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
			String username = claims.getSubject();
			Object authoritiesClaims = claims.get("authorities");
			
			/* *
			 * *VALIDAMOS QUE TAMBIEN EXISTA EN LA WHITE_LIST
			 * */
			if(WHITE_LIST.get(username) == null) {
				DtoResponse<String> dtoResp = new DtoResponse<>();
				dtoResp.setCode_status(1);
				dtoResp.setMessage("El token es invalido. Inicia sesión de nuevo");

				response.getWriter().write(new ObjectMapper().writeValueAsString(dtoResp));
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.setContentType(CONTENT_TYPE);
				return;
			}

			Collection<? extends GrantedAuthority> authorities = Arrays.asList(
					new ObjectMapper()
					.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
					.readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
					);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			chain.doFilter(request, response);
		} catch (Exception e) {
			DtoResponse<String> dtoResp = new DtoResponse<>();
			dtoResp.setCode_status(1);
			dtoResp.setMessage("El token es invalido. Inicia sesión de nuevo");

			response.getWriter().write(new ObjectMapper().writeValueAsString(dtoResp));
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(CONTENT_TYPE);
		}
	}

	
}
