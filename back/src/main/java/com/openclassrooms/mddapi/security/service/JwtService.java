package com.openclassrooms.mddapi.security.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
	
	private JwtEncoder jwtEncoder;
	
	public JwtService(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}
	
	public String generateToken(String authenticationName) {
		Instant now = Instant.now();		
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plus(1, ChronoUnit.DAYS))
				.subject(authenticationName)
				.build();
		JwtEncoderParameters jwtEncoderParameters =
				JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
		return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
	}
}
