package com.kakaopay.todolist.todolist.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.kakaopay.todolist.todolist.domain.Todo;

public interface TodoService {
	public Todo find(@PathVariable Long id);
	
	public Page<Todo> findAll(Pageable pageable);
	
	public Todo register(@RequestBody Todo todo);
	
	public Todo modify(@RequestBody Todo todo);
	
	public void remove(@PathVariable Long id);
}
