package com.kakaopay.todolist.todolist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.todolist.todolist.domain.RefTodo;
import com.kakaopay.todolist.todolist.domain.RefTodoPk;

public interface RefTodoRepository extends CrudRepository<RefTodo, RefTodoPk>{
	List<RefTodo> findByTodoId(@Param("todoId") Long todoId);
	
	@Transactional
	void removeByTodoId(@Param("todoId") Long todoId);
}
