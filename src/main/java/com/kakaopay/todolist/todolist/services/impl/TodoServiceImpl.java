package com.kakaopay.todolist.todolist.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kakaopay.todolist.todolist.domain.Todo;
import com.kakaopay.todolist.todolist.repository.TodoRepository;
import com.kakaopay.todolist.todolist.services.TodoService;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoRepository todoRepository;
	
	@Override
	public Todo find(Long id) {
		return todoRepository.findById(id).orElse(null);
	}

	@Override
	public Page<Todo> findAll(Pageable pageable) {
		return todoRepository.findAll(pageable);
	}

	@Override
	public Todo register(Todo todo) {
		//ID는 자동입력되어 확인이 필요 없음.
		return todoRepository.save(todo);
	}

	@Override
	public Todo modify(Todo todo) {
		Todo result = find(todo.getId());
		
		if(result == null) {
			return null;
		}
		
		return todoRepository.save(todo);
	}

	@Override
	public void remove(Long id) {
		todoRepository.deleteById(id);
	}

}
