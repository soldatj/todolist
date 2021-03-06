package com.kakaopay.todolist.todolist.services;

import java.util.List;

import com.kakaopay.todolist.todolist.domain.RefTodoMap;

public interface RefTodoMapService {
	public List<RefTodoMap> findByTodoId(Long todoId);
	
	public List<RefTodoMap> findByRefTodoId(Long refTodoId);
	
	public List<RefTodoMap> registerSameTodoIdList(Long todoId, List<RefTodoMap> refTodoList);
	
	public void removeByTodoId(Long todoId);
}
