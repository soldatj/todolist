package com.kakaopay.todolist.todolist.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("name", "chang!!!");
		return "index";
	}
}
