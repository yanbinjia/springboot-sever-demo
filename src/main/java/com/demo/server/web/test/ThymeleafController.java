package com.demo.server.web.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.server.interceptor.TokenPass;

@Controller
@RequestMapping("/test/thym")
public class ThymeleafController {

	@TokenPass
	@RequestMapping("/test")
	public String testForThymeleaf(Model model) {
		Map<String, String> map = new HashMap<>();
		map.put("name", "John");
		map.put("id", "happy_john");
		model.addAttribute("user", map);
		return "hello";
	}

}
