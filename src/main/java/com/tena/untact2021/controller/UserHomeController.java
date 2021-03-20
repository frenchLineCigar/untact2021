package com.tena.untact2021.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserHomeController {

	@RequestMapping("/user/home/main")
	@ResponseBody
	public String showMain() {
		return "2021 Untact Project";
	}

}
