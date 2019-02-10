package com.kakaopay.todolist.todolist.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.todolist.todolist.domain.Request;
import com.kakaopay.todolist.todolist.domain.Result;
import com.kakaopay.todolist.todolist.domain.Todo;
import com.kakaopay.todolist.todolist.services.TodoService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/api/todo")
@Slf4j
public class TodoController {
	private TodoService todoService;
	
	@Autowired
	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	}
	
	@GetMapping("/{id}")
	public Result<Todo> find(@PathVariable Long id) {
		log.info("find : " + id);
		
		Result<Todo> result = new Result<>();
		Todo todo = todoService.find(id);
		if(todo == null) {
			result.setErrorCode(HttpStatus.NOT_FOUND.value());
			result.setErrorMessage("Not found");
		}else {
			result.setResult(todo);
		}
		
		return result;
	}
	
	@GetMapping
	public Result<Page<Todo>> findAll(@PageableDefault(size=10, sort="id",direction = Sort.Direction.DESC) Pageable pageable) {
		log.info("findAll");
		
		Result<Page<Todo>> result = new Result<>();
		Page<Todo> page = todoService.findAll(pageable);
		if(page == null) {
			result.setErrorCode(HttpStatus.NOT_FOUND.value());
			result.setErrorMessage("Not found");
		}else {
			result.setResult(page);
		}
		
		return result;
	}
	
	@PostMapping
	public Result<Long> register(@RequestBody Request request) {
		log.info("register : " + request);
		
		
		Result<Long> result = new Result<Long>();
		Todo todo = todoService.register(request.getTodo());
		if(todo == null) {
			result.setErrorCode(HttpStatus.BAD_REQUEST.value());
			result.setErrorMessage("Already Exists");
		}else {
			result.setResult(todo.getId());
		}
		
		return result;
	}
	
	@PutMapping
	public Result<Long> modify(@RequestBody Request request) {
		log.info("modify : " + request);
		
		Result<Long> result = new Result<Long>();
		Todo todo = todoService.modify(request.getTodo());
		if(todo == null) {
			result.setErrorCode(HttpStatus.BAD_REQUEST.value());
			result.setErrorMessage("Not Found");
		}else {
			result.setResult(todo.getId());
		}
		
		return result;
	}
	
	@DeleteMapping("/{id}")
	public Result<Long> remove(@PathVariable Long id) {
		log.info("remove : " + id);
		todoService.remove(id);
		return new Result<Long>();
	}
}
