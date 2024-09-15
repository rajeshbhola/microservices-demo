package com.rajesh.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "user-microservice", url = "http://localhost:8081")
public interface UserServiceFeignClient {

	@GetMapping("/api/users/validate")
	Boolean validateToken(@RequestHeader("Authorization") String token);

	@GetMapping("/api/users/roles")
	String getUserRoles(@RequestHeader("Authorization") String token);
}
