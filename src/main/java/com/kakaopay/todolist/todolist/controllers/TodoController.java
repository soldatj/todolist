package com.kakaopay.todolist.todolist.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.todolist.todolist.domain.Result;
import com.kakaopay.todolist.todolist.domain.Todo;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/api/todo")
@Slf4j
public class TodoController {
	
	@GetMapping("/{id}")
	public Result<Todo> find(@PathVariable String id) {
		log.info("find : " + id);
		return new Result<Todo>();
	}
	
	@GetMapping
	public Result<Page<Todo>> findAll(Pageable pageable) {
		log.info("findAll");
		return new Result<Page<Todo>>();
	}
	
	@PostMapping
	public Result<String> register(@RequestBody Todo newTodo) {
		log.info("register : " + newTodo);
		return new Result<String>();
	}
	
	@PutMapping
	public Result<String> modify(@RequestBody Todo newTodo) {
		log.info("modify : " + newTodo);
		return new Result<String>();
	}
	
	@DeleteMapping
	public Result<String> remove(@PathVariable String id) {
		log.info("remove : " + id);
		return new Result<String>();
	}
}
