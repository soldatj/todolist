package com.kakaopay.todolist.todolist.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kakaopay.todolist.todolist.domain.Todo;

public interface TodoService {
	public Todo find(Long id);
	
	public List<Todo> findByIdNotAndContentLike(Long id, String content);
	
	public Page<Todo> findAll(Pageable pageable);
	
	public List<Todo> findRefTodoListById(Long id);
	
	public Todo register(Todo todo);
	
	public Todo modify(Todo todo);
	
	public void remove(Long id);
}
