package com.kakaopay.todolist.todolist.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.kakaopay.todolist.todolist.domain.Todo;

public interface TodoService {
	public Todo find(@PathVariable Long id);
	
	public List<Todo> findByIdNotAndContentLike(Long id, String content);
	
	public Page<Todo> findAll(Pageable pageable);
	
	public Todo register(Todo todo);
	
	public Todo modify(Todo todo);
	
	public void remove(Long id);
}
