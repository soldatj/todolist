package com.kakaopay.todolist.todolist.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

	@RequestMapping("/")
	public String index(Model model) {
		return "index";
	}
	
	@RequestMapping("/swagger")
	public String swagger() {
		return "redirect:/swagger-ui.html";
	}
}
