package com.portal.security;

import java.util.HashMap;

public class TokenJwtConfig {

	public static final String SECRET_KEY = "dGllbmRhLWVuLWxpbmVhLTIwMjQtZGVtbyBwcnVlYmEgZGUgY2xhdmUgcGFyYSBqd3Q=";
	public static final String PREFIX_TOKEN = "Bearer ";
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String CONTENT_TYPE = "application/json";
//	public static final Integer TOKEN_LIFETIME = 3600000; //1 hora
//	public static final Integer TOKEN_LIFETIME =   1800000; // 30 min
	public static final Integer TOKEN_LIFETIME =   30000; // 5 min
//	public static final Integer TOKEN_LIFETIME =   6000; // 1 min

	public static final HashMap<String, String> WHITE_LIST = new HashMap<>();
}
