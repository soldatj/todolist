package com.kakaopay.todolist.todolist.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakaopay.todolist.todolist.domain.RefTodoMap;
import com.kakaopay.todolist.todolist.domain.Todo;
import com.kakaopay.todolist.todolist.exception.NotExistTodoRefDataException;
import com.kakaopay.todolist.todolist.repository.RefTodoMapRepository;
import com.kakaopay.todolist.todolist.services.RefTodoMapService;
import com.kakaopay.todolist.todolist.services.TodoService;

@Service
public class RefTodoMapServiceImpl implements RefTodoMapService {
	@Autowired
	private TodoService todoService;
	
	@Autowired
	private RefTodoMapRepository refTodoRepository;
	
	@Override
	public List<RefTodoMap> findAllByTodoId(Long id) {
		return refTodoRepository.findByTodoId(id);
	}
	
	@Override
	public List<RefTodoMap> registerSameTodoIdList(Long todoId, List<RefTodoMap> refTodoList) {
		List<RefTodoMap> returnList = new ArrayList<RefTodoMap>();
		
		if(refTodoList!=null && !refTodoList.isEmpty()) {
			//삭제 후 재입력
			removeByTodoId(todoId);
			
			for(RefTodoMap refData : refTodoList) {
				Long refTodoId = refData.getRefTodoId();
				
				Todo refTodo = todoService.find(refData.getRefTodoId());
				
				//TODO Exception 생성 및 캐치
				//존재하지않는 Todo 데이터가 입력되는 경우 처리
				if(refTodoId == null || refTodo == null) {
					throw new NotExistTodoRefDataException(refTodoId);
				}
				
				refData.setTodoId(todoId);
				
				RefTodoMap returnRefTodo = refTodoRepository.save(refData);
				returnList.add(returnRefTodo);
			}
		}
		
		return returnList;
	}

	@Override
	public void removeByTodoId(Long id) {
		refTodoRepository.removeByTodoId(id);
	}
	
	public void validExistsRefDataTodos() {
		
	}

}
