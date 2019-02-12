package com.kakaopay.todolist.todolist.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kakaopay.todolist.todolist.domain.RefTodoMap;
import com.kakaopay.todolist.todolist.domain.Todo;
import com.kakaopay.todolist.todolist.repository.TodoRepository;
import com.kakaopay.todolist.todolist.services.RefTodoMapService;
import com.kakaopay.todolist.todolist.services.TodoService;

@Service
public class TodoServiceImpl implements TodoService {
	@Autowired
	private RefTodoMapService refTodoMapService;
	
	@Autowired
	private TodoRepository todoRepository;
	
	@Override
	public Todo find(Long id) {
		Todo todo =  todoRepository.findById(id).orElse(null);
		
		if(todo != null) {
			//참조 데이터 테이블을 조회
			List<RefTodoMap> refDataList = refTodoMapService.findAllByTodoId(id);
			
			//content의 뒤쪽으로 "방청소 @1 @3" 형태와 같이 RefTodo의 id를 기록하기 위한 SB
			StringBuffer contentAndRefTodoId = new StringBuffer();
			contentAndRefTodoId.append(todo.getContent());
			
			List<Todo> refTodoList = new ArrayList<Todo>();
			
			if(refDataList != null) {
				//참조 데이터 테이블 정보를 바탕으로 참조 Todo 리스트를 조회
				for(RefTodoMap refTodo : refDataList) {
					Long refTodoId = refTodo.getRefTodoId();
					
					Todo refDtlTodo =  todoRepository.findById(refTodoId).orElse(null);
					contentAndRefTodoId.append(" ").append("@").append(refDtlTodo.getId());
					refTodoList.add(refDtlTodo);
				}
			}
			
			todo.setRefTodoList(refTodoList);
			todo.setContentAndRefTodoId(contentAndRefTodoId.toString());
		}
		
		return todo;
	}
	
	@Override
	public List<Todo> findByIdNotAndContentLike(Long id, String content) {
		String contentLikeStr = "%" + content + "%";
		
		return todoRepository.findByIdNotAndContentLike(id, contentLikeStr);
	}

	@Override
	public Page<Todo> findAll(Pageable pageable) {
		return todoRepository.findAll(pageable);
	}

	@Override
	public Todo register(Todo todo) {
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

	@Override
	public List<Todo> findRefTodoListById(Long id) {
		//참조 데이터 테이블을 조회
		List<RefTodoMap> refDataList = refTodoMapService.findAllByTodoId(id);
		
		//참조 데이터 테이블 정보를 바탕으로 참조 Todo 리스트를 조회
		List<Todo> refTodoList = new ArrayList<Todo>();
		if(refDataList != null) {
			for(RefTodoMap refTodo : refDataList) {
				Long refTodoId = refTodo.getRefTodoId();
				
				Todo refDtlTodo =  todoRepository.findById(refTodoId).orElse(null);
				refTodoList.add(refDtlTodo);
			}
		}
		
		return refTodoList;
	}

	@Override
	public Todo modifyComplete(Long id, String compYn) {
		Todo todo =  todoRepository.findById(id).orElse(null);
		todo.setCompYn(compYn);
		
		Todo result = modify(todo);
		
		if(result == null) {
			return null;
		}
		
		return result;
	}
}
