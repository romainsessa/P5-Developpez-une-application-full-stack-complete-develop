package com.openclassrooms.mddapi.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.openclassrooms.mddapi.security")
public class SecurityProperties {

	private String jwtKey;
	
	public String getJwtKey() {
		return jwtKey;
	}
	
	public void setJwtKey(String jwtKey) {
		this.jwtKey = jwtKey;
	}

}