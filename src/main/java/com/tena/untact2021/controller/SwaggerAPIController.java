package com.tena.untact2021.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@Controller
public class SwaggerAPIController {

	@ResponseStatus(value = HttpStatus.MOVED_PERMANENTLY)
	@GetMapping(value = {"/api-ui", "/swagger-ui.html"})
	public String showSwaggerUI() {
		log.info("SwaggerAPIController.showSwaggerUI");

		return "redirect:/swagger-ui/";
	}
}
