package com.kakaopay.todolist.todolist.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.todolist.todolist.domain.RefTodoMap;
import com.kakaopay.todolist.todolist.domain.Todo;
import com.kakaopay.todolist.todolist.exception.CrossReferenceException;
import com.kakaopay.todolist.todolist.exception.NotExistTodoRefDataException;
import com.kakaopay.todolist.todolist.repository.RefTodoMapRepository;
import com.kakaopay.todolist.todolist.services.RefTodoMapService;
import com.kakaopay.todolist.todolist.services.TodoService;

@Service
@Transactional
public class RefTodoMapServiceImpl implements RefTodoMapService {
	@Autowired
	private TodoService todoService;
	
	@Autowired
	private RefTodoMapRepository refTodoRepository;
	
	@Override
	public List<RefTodoMap> findByTodoId(Long id) {
		return refTodoRepository.findByTodoId(id);
	}
	
	@Override
	public List<RefTodoMap> findByRefTodoId(Long id) {
		return refTodoRepository.findByRefTodoId(id);
	}
	
	@Override
	public List<RefTodoMap> registerSameTodoIdList(Long todoId, List<RefTodoMap> refTodoList){
		List<RefTodoMap> returnList = new ArrayList<RefTodoMap>();
		
		removeByTodoId(todoId);
		
		if(refTodoList == null) {
			return returnList;
		}
		
		for(RefTodoMap refData : refTodoList) {
			Long refTodoId = refData.getRefTodoId();
			
			Todo refTodo = todoService.find(refData.getRefTodoId());
			
			//존재하지 않는 Todo 데이터가 입력되는 경우 예외발생
			//todoId와 refTodoId가 동일한 경우에도 같은 예외 발행(즉 입력되기전의 할일을 입력한 경우)
			if(refTodoId == null || refTodo == null || todoId.equals(refTodoId)) {
				throw new NotExistTodoRefDataException(refTodoId);
			}
			
			//참조 할일 대상이 참조하는 대상을 찾아 파라미터 todoId가 포함되어있는지 count를 기록 
			long refCnt = findByTodoId(refTodoId).stream().filter(rtm->rtm.getRefTodoId().equals(todoId)).count();
			
			//할일간 상호참조가 발생되면 예외발생
			if(refCnt > 0) {
				throw new CrossReferenceException(todoId, refTodoId);
			}
			
			refData.setTodoId(todoId);
			
			RefTodoMap returnRefTodo = refTodoRepository.save(refData);
			returnList.add(returnRefTodo);
		}
		
		return returnList;
	}

	@Override
	public void removeByTodoId(Long id) {
		refTodoRepository.removeByTodoId(id);
	}
	
}
