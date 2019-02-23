package com.kakaopay.todolist.todolist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.kakaopay.todolist.todolist.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long>{
	List<Todo> findByIdLike(@Param("id") String id);
	
	Todo findOneByContent(@Param("content") String content);
	
	List<Todo> findByContentLike(String content);
	
	//Todo 검색시 지정된 id를 제외하고 컨텐츠 내용을 Like 검색
	List<Todo> findByIdNotAndContentLike(@Param("id") Long id, @Param("content") String content);
}
