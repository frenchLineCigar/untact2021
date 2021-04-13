package com.tena.untact2021.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminHomeController {

	@RequestMapping("/admin/home/main")
	public String showMain() {
		return "/admin/home/main";
	}

}
