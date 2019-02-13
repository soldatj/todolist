package com.kakaopay.todolist.todolist.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.todolist.todolist.domain.Result;
import com.kakaopay.todolist.todolist.domain.Todo;
import com.kakaopay.todolist.todolist.exception.AlreadyExistsTodoException;
import com.kakaopay.todolist.todolist.exception.NotExistTodoException;
import com.kakaopay.todolist.todolist.services.TodoService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/api/todo")
@Slf4j
public class TodoController {
	private TodoService todoService;
	
	@Autowired
	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping
	public Result<Page<Todo>> findAll(@PageableDefault(size=10, sort="id",direction = Sort.Direction.DESC) Pageable pageable) {
		log.info("findAll");
		
		Result<Page<Todo>> result = new Result<>();
		Page<Todo> page = todoService.findAll(pageable);
		if(page != null) {
			result.setResult(page);
		}
		
		return result;
	}
	
	@PostMapping
	public Result<Long> register(@RequestBody Todo todo) {
		log.info("register : " + todo);
		
		Result<Long> result = new Result<Long>();
		Todo returnTodo = todoService.register(todo);
		
		if(returnTodo == null) {
			throw new AlreadyExistsTodoException();
		}else {
			result.setResult(returnTodo.getId());
		}
		
		return result;
	}
	
	//Todo 원본을 조회해 모달창으로 입력된 수정사항만 수정(content, refTodoMapList)
	@PutMapping(value = "/modal/")
	public Result<Long> modifyModal(@RequestBody Todo todo) {
		log.info("modify : " + todo);
		
		Result<Long> result = new Result<Long>();
		Todo returnTodo = todoService.modifyModal(todo);
		
		if(returnTodo == null) {
			throw new NotExistTodoException(todo.getId());
		}else {
			result.setResult(returnTodo.getId());
		}
		
		return result;
	}
	
	//완료 상태로 상태 수정
	@PutMapping(value = "/complete/{id}")
	public Result<Long> complete(@PathVariable Long id) {
		log.info("complate : " + id);
		
		Result<Long> result = new Result<Long>();
		Todo returnTodo = todoService.modifyCompYn(id, "Y");
		
		if(returnTodo == null) {
			throw new NotExistTodoException(id);
		}else {
			result.setResult(returnTodo.getId());
		}
		
		return result;
	}
	
	//완료 취소 상태로 상태 수정
	@PutMapping(value = "/cancel/{id}")
	public Result<Long> cancel(@PathVariable Long id) {
		log.info("cancel : " + id);
		
		Result<Long> result = new Result<Long>();
		Todo returnTodo = todoService.modifyCompYn(id, "N");
		
		if(returnTodo == null) {
			throw new NotExistTodoException(id);
		}else {
			result.setResult(returnTodo.getId());
		}
		
		return result;
	}
	
	//Todo 중 검색 id가 포함되있지 않고 컨텐트 전후방 라이크 검색
	@GetMapping(value = "/findByIdNotAndContentLike")
	public Result<List<Todo>> findByIdNotAndContentLike(@RequestParam(value = "id", defaultValue = "-1")Long id,
			@RequestParam(value = "content", defaultValue = "") String content ) {
		log.info("findByIdNotAndContentLike : " + id);
		
		Result<List<Todo>> result = new Result<>();
		List<Todo> todoList = todoService.findByIdNotAndContentLike(id, content);
		result.setResult(todoList);
		
		return result;
	}
}
