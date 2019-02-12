package com.kakaopay.todolist.todolist.services;

import java.util.List;

import com.kakaopay.todolist.todolist.domain.RefTodo;

public interface RefTodoService {
	public List<RefTodo> findAllByTodoId(Long todoId);
	
	public List<RefTodo> registerList(List<RefTodo> refTodoList);
	
	public void removeByTodoId(Long todoId);
}
