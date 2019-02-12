package com.kakaopay.todolist.todolist.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakaopay.todolist.todolist.domain.RefTodo;
import com.kakaopay.todolist.todolist.repository.RefTodoRepository;
import com.kakaopay.todolist.todolist.services.RefTodoService;

@Service
public class RefTodoServiceImpl implements RefTodoService {

	@Autowired
	private RefTodoRepository refTodoRepository;
	
	@Override
	public List<RefTodo> findAllByTodoId(Long id) {
		return refTodoRepository.findByTodoId(id);
	}
	
	@Override
	public List<RefTodo> registerList(List<RefTodo> refTodoList) {
		List<RefTodo> returnList = new ArrayList<RefTodo>();
		
		if(refTodoList!=null && !refTodoList.isEmpty()) {
			//삭제 후 재입력
			removeByTodoId(refTodoList.get(0).getTodoId());
			
			for(RefTodo refTodo : refTodoList) {
				RefTodo returnRefTodo = refTodoRepository.save(refTodo);
				returnList.add(returnRefTodo);
			}
		}
		
		return returnList;
	}

	@Override
	public void removeByTodoId(Long id) {
		refTodoRepository.removeByTodoId(id);
	}

}
