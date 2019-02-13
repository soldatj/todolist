package com.kakaopay.todolist.todolist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.todolist.todolist.domain.RefTodoMap;
import com.kakaopay.todolist.todolist.domain.RefTodoMapPk;

public interface RefTodoMapRepository extends CrudRepository<RefTodoMap, RefTodoMapPk>{
	List<RefTodoMap> findByTodoId(@Param("todoId") Long todoId);
	
	List<RefTodoMap> findByRefTodoId(@Param("refTodoId") Long refTodoId);
	
	@Transactional
	void removeByTodoId(@Param("todoId") Long todoId);
}
