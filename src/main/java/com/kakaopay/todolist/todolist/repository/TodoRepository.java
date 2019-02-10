package com.kakaopay.todolist.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakaopay.todolist.todolist.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long>{

}
