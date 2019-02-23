package com.kakaopay.todolist.todolist.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.todolist.todolist.domain.RefTodoMap;
import com.kakaopay.todolist.todolist.domain.Todo;
import com.kakaopay.todolist.todolist.exception.AlreadyExistsTodoException;
import com.kakaopay.todolist.todolist.exception.ExistCompleteRefToMeTodoException;
import com.kakaopay.todolist.todolist.exception.ExistNotCompleteRefTodoException;
import com.kakaopay.todolist.todolist.exception.NotExistTodoException;
import com.kakaopay.todolist.todolist.exception.NotExistTodoRefDataException;
import com.kakaopay.todolist.todolist.repository.TodoRepository;
import com.kakaopay.todolist.todolist.services.RefTodoMapService;
import com.kakaopay.todolist.todolist.services.TodoService;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {
	@Autowired
	private RefTodoMapService refTodoMapService;
	
	@Autowired
	private TodoRepository todoRepository;
	
	@Override
	public Todo find(Long id) {
		Todo todo =  todoRepository.findById(id).orElse(null);
		
		if(id == null || todo == null) {
			throw new NotExistTodoException(id);
		}
		
		setTodoRefTodoIds(todo);
		
		return todo;
	}
	
	@Override
	public List<Todo> findByIdNotAndContentLike(Long id, String content) {
		String contentLikeStr = "%" + content + "%";
		
		return todoRepository.findByIdNotAndContentLike(id, contentLikeStr);
	}
	
	@Override
	public Page<Todo> findAll(Pageable pageable) {
		Page<Todo> page = todoRepository.findAll(pageable);
		
		for(Todo todo : page) {
			setTodoRefTodoIds(todo);
		}
		
		return page;
	}

	@Override
	public Todo register(Todo todo) {
		Todo returnTodo = todoRepository.save(todo);
		
		if(returnTodo == null) {
			throw new AlreadyExistsTodoException();
		}
		
		List<RefTodoMap> refTodoList = todo.getRefTodoMapList();
		refTodoMapService.registerSameTodoIdList(returnTodo.getId(), refTodoList);
		
		return returnTodo;
	}

	@Override
	public Todo modify(Todo todo) {
		Todo returnTodo = todoRepository.save(todo);
		
		if(returnTodo == null) {
			throw new NotExistTodoException(todo.getId());
		}
		
		List<RefTodoMap> refTodoList = todo.getRefTodoMapList();
		refTodoMapService.registerSameTodoIdList(returnTodo.getId(), refTodoList);
		
		return returnTodo;
	}
	
	@Override
	public Todo modifyModal(Todo todo) {
		Todo findTodo = find(todo.getId());
		
		if(findTodo == null) {
			throw new NotExistTodoException(todo.getId());
		}
		
		findTodo.setContent(todo.getContent());
		findTodo.setRefTodoMapList(todo.getRefTodoMapList());
		
		Todo returnTodo = todoRepository.save(findTodo);
		
		List<RefTodoMap> refTodoList = findTodo.getRefTodoMapList();
		refTodoMapService.registerSameTodoIdList(returnTodo.getId(), refTodoList);
		
		return returnTodo;
	}

	@Override
	public void remove(Long id) {
		Todo findTodo = find(id);
		
		if(findTodo == null) {
			throw new NotExistTodoException(id);
		}
		
		todoRepository.deleteById(id);
	}

	@Override
	public Todo modifyCompYn(Long id, String compYn) {
		Todo todo =  todoRepository.findById(id).orElse(null);
		
		if(todo == null) {
			throw new NotExistTodoException(id);
		}
		
		//완료 상태로 전환시에 참조 할일들이 완료상태인지 체크
		if("Y".equals(compYn)) {
			validNotExistsNotCompleteRefTodos(id);
		}else if("N".equals(compYn)) {
			validNotExistsNotCompleteRefToMeTodos(id);
		}
		
		todo.setCompYn(compYn);
		Todo result = modify(todo);
		
		if(result == null) {
			return null;
		}
		
		return result;
	}
	
	public Todo setTodoRefTodoIds(Todo todo) {
		Long id = todo.getId();
		
		//content의 뒤쪽으로 "방청소 @1 @3" 형태와 같이 RefTodo의 id를 기록하기 위한 SB
		StringBuffer sbContentAndRefTodoIds = new StringBuffer();
		sbContentAndRefTodoIds.append(todo.getContent());
		
		//"1, 3"과 같이 RefTodo의 id를 기록하기 위한 SB
		StringBuffer sbRefTodoIds = new StringBuffer();
		
		//참조 데이터 테이블을 조회
		List<RefTodoMap> refDataList = refTodoMapService.findByTodoId(id);
		
		if(refDataList != null) {
			//참조 데이터 테이블 정보를 바탕으로 참조 Todo 리스트를 조회
			for(RefTodoMap refTodo : refDataList) {
				Long refTodoId = refTodo.getRefTodoId();
				
				Todo refDtlTodo = todoRepository.findById(refTodoId).orElse(null);
				
				if(refDtlTodo == null) {
					throw new NotExistTodoRefDataException(refTodoId);
				}
				
				sbContentAndRefTodoIds.append(" ").append("@").append(refDtlTodo.getId());
				sbRefTodoIds.append(refDtlTodo.getId()).append(",");
			}
		}
		
		todo.setRefTodoMapList(refDataList);
		todo.setContentAndRefTodoIds(sbContentAndRefTodoIds.toString());
		todo.setRefTodoIds(sbRefTodoIds.toString());
		
		return todo;
	}
	
	//완료하기전에 자신이 참조하는 다른 할일이 완료되어있는지 확인
	public void validNotExistsNotCompleteRefTodos(Long id) {
		//참조 데이터 테이블을 조회
		List<RefTodoMap> refDataList = refTodoMapService.findByTodoId(id);
		
		if(refDataList != null) {
			//참조 데이터 테이블 정보를 바탕으로 참조 Todo 리스트를 조회
			for(RefTodoMap refTodo : refDataList) {
				Long refTodoId = refTodo.getRefTodoId();
				
				Todo refDtlTodo =  todoRepository.findById(refTodoId).orElse(null);
				
				//참조 데이터가 N 인 경우 Exception 발생
				if("N".equals(refDtlTodo.getCompYn())) {
					throw new ExistNotCompleteRefTodoException(refTodoId);
				}
			}
		}
	}
	
	//완료를 캔슬하기전에 자신을 참조하는 다른 할일이 완료 상태가 아닌지 확인
	public void validNotExistsNotCompleteRefToMeTodos(Long id) {
		//참조 데이터 테이블을 조회
		List<RefTodoMap> refToMeDataList = refTodoMapService.findByRefTodoId(id);
		
		if(refToMeDataList != null) {
			//참조 데이터 테이블 정보를 바탕으로 참조 Todo 리스트를 조회
			for(RefTodoMap refTodo : refToMeDataList) {
				Long refToMeTodoId = refTodo.getTodoId();
				
				Todo refDtlTodo =  todoRepository.findById(refToMeTodoId).orElse(null);
				
				//참조 데이터가 N 인 경우 Exception 발생
				if("Y".equals(refDtlTodo.getCompYn())) {
					throw new ExistCompleteRefToMeTodoException(refToMeTodoId);
				}
			}
		}
	}
}
