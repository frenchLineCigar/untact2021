package com.tena.untact2021.controller;

import com.tena.untact2021.util.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class UserHomeController {

	@RequestMapping("/user/home/main")
	@ResponseBody
	public String showMain() {
		return "2021 Untact Project";
	}

    @RequestMapping("/user/home/doFormTest")
    @ResponseBody
    public Map<String, Object> doFormTest(String name, int age) {
        return Util.mapOf("name", name, "age", age);
    }

}
