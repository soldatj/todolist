package com.kakaopay.todolist.todolist.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodolistController {

	@GetMapping("/findAll")
	public String findAll() {
		return "findAll";
	}
}
