package com.kakaopay.todolist.todolist.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kakaopay.todolist.todolist.domain.RefTodoMap;
import com.kakaopay.todolist.todolist.domain.Todo;
import com.kakaopay.todolist.todolist.exception.NotExistTodoException;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoServiceTest {

	@Autowired
	private TodoService todoService;
	
	@Autowired
	private RefTodoMapService refTodoMapService;

	@Test
	public void givenTodo_whenFind_thenReturnTodo() throws Exception{
		//given
		Todo todo = new Todo();
		todo.setContent("호텔 예약하기");
		todo.setCompYn("N");
		
		Todo result = todoService.register(todo);
		assertThat(result).isNotNull();
		
		//when
		Todo resultTodo = todoService.find(todo.getId());
		
		//then
		assertThat(resultTodo).isNotNull();
		assertThat(resultTodo.getId()).isNotNull();
		
		Page<Todo> resultSize10 = todoService.findAll(PageRequest.of(0, 10));
		
		resultSize10.getSize();
	}
	
	@Test
	public void givenTodo_whenModify_thenReturnTodo() throws Exception{
		//given
		Todo todo = new Todo();
		todo.setContent("호텔 예약하기");
		todo.setCompYn("N");
		
		Todo result = todoService.register(todo);
		assertThat(result).isNotNull();
		
		//when
		String modifyContent = "회의실 예약하기";
		String modifyCompYn = "Y";
		
		todo.setContent(modifyContent);
		todo.setCompYn(modifyCompYn);
		
		Todo resultTodo = todoService.modify(todo);
		
		//then
		assertThat(resultTodo).isNotNull();
		assertThat(resultTodo.getContent()).isEqualTo(modifyContent);
		assertThat(resultTodo.getCompYn()).isEqualTo(modifyCompYn);
	}
	
	@Test(expected = NotExistTodoException.class)
	public void givenTodo_whenRemoveAndFind_thenReturnNull() throws Exception{
		//given
		Todo todo = new Todo();
		todo.setContent("호텔 예약하기");
		todo.setCompYn("N");
		
		Todo result = todoService.register(todo);
		assertThat(result).isNotNull();
		
		//when
		Todo resultTodo = todoService.find(result.getId());
		assertThat(resultTodo).isNotNull();
		
		todoService.remove(resultTodo.getId());
		
		resultTodo = todoService.find(resultTodo.getId());
		
		//then
		assertThat(resultTodo).isNull();
		
		Page<Todo> resultSize10 = todoService.findAll(PageRequest.of(0, 10));
		
		resultSize10.getSize();
	}
	
	@Test
	public void whenRegister_thenReturnTodo() throws Exception{
		//when
		Todo todo = new Todo();
		todo.setContent("비행기 예약하기");
		todo = todoService.register(todo);
		
		Todo todoR1 = new Todo();
		todoR1.setContent("호텔 예약하기");
		todoR1 = todoService.register(todoR1);
		
		Todo todoR2 = new Todo();
		todoR2.setContent("맛집 예약하기");
		todoR2 = todoService.register(todoR2);
		
		List<RefTodoMap> refTodoList = new ArrayList<RefTodoMap>();
		
		RefTodoMap refTodo1 = new RefTodoMap();
		refTodo1.setRefTodoId(todoR1.getId());
		refTodoList.add(refTodo1);
		
		RefTodoMap refTodo2 = new RefTodoMap();
		refTodo2.setRefTodoId(todoR2.getId());
		refTodoList.add(refTodo2);
		
		refTodoMapService.registerSameTodoIdList(todo.getId(), refTodoList);
		
		List<RefTodoMap> resultSize2 = refTodoMapService.findByTodoId(todo.getId());
		
		//then
		assertThat(todo).isNotNull();
		assertThat(todo.getId()).isNotNull();
		assertThat(resultSize2.size()).isEqualTo(2);
	}
	
	@Test
	public void whenRegisterListAndFindAll_thenReturnTodoList() throws Exception{
		//when
		for(int i=0; i<100; i++) {
			Todo todo = new Todo();
			todo.setContent("todo : " + i);
			todoService.register(todo);
		}
		
		Page<Todo> resultSize10 = todoService.findAll(PageRequest.of(0, 10));
		Page<Todo> resultSize20 = todoService.findAll(PageRequest.of(0, 20));
		
		//then
		assertThat(resultSize10.getSize()).isEqualTo(10);
		assertThat(resultSize20.getSize()).isEqualTo(20);
	}
	
	@Test
	public void whenRemoveAndRegister_thenReturnRefTodoList() throws Exception{
		//when
		//할일1 입력
		Todo todo = new Todo();
		todo.setContent("비행기 예약하기");
		todo = todoService.register(todo);
		
		//할일2 입력시 1의 정보를 이용해 RefTodoMap을 구성해서 입력
		Todo todo2 = new Todo();
		todo2.setContent("호텔 예약하기");
		
		RefTodoMap refTodo1 = new RefTodoMap();
		refTodo1.setRefTodoId(todo.getId());
		
		List<RefTodoMap> refTodoList = new ArrayList<RefTodoMap>();
		refTodoList.add(refTodo1);
		todo2.setRefTodoMapList(refTodoList);
		
		todo2 = todoService.register(todo2);
		
		assertThat(todo2).isNotNull();
		assertThat(todo2.getId()).isNotNull();
		
		List<RefTodoMap> resultSize1 = refTodoMapService.findByTodoId(todo2.getId());
		assertThat(resultSize1.size()).isEqualTo(1);
		
		//할일1에 RefTodo 목록 입력
		List<RefTodoMap> refTodoList2 = new ArrayList<RefTodoMap>();
		for(int i=0; i<90; i++) {
			Todo todoF = new Todo();
			todoF.setContent("todoF : " + i);
			todoService.register(todoF);
			
			RefTodoMap refTodoF = new RefTodoMap();
			refTodoF.setRefTodoId(todoF.getId());
			
			refTodoList2.add(refTodoF);
		}
		
		todo.setRefTodoMapList(refTodoList2);
		todoService.modify(todo);
		
		//then
		assertThat(todo).isNotNull();
		assertThat(todo.getId()).isNotNull();
		
		List<RefTodoMap> resultRefTodosSize90 = refTodoMapService.findByTodoId(todo.getId());
		assertThat(resultRefTodosSize90.size()).isEqualTo(90);
	}
	
}
