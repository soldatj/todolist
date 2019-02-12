package com.kakaopay.todolist.todolist.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kakaopay.todolist.todolist.domain.RefTodo;
import com.kakaopay.todolist.todolist.domain.Todo;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoServiceTest {

	@Autowired
	private TodoService todoService;
	
	@Autowired
	private RefTodoService refTodoService;

	private Todo setUpTodo = null;
	
	@Before
	public void setUpClass() throws Exception{
		Todo todo = new Todo();
		todo.setContent("호텔 예약하기");
		todo.setCompYn("N");
		
		Todo result = todoService.register(todo);
		assertThat(result).isNotNull();
		
		this.setUpTodo = result;
	}
	
	@Test
	public void givenId_whenFind_thenReturnTodo() throws Exception{
		//given
		Long id = this.setUpTodo.getId();
		
		//when
		Todo resultTodo = todoService.find(id);
		
		//then
		assertThat(resultTodo).isNotNull();
		assertThat(resultTodo.getId()).isNotNull();
		
		Page<Todo> resultSize10 = todoService.findAll(PageRequest.of(0, 10));
		
		resultSize10.getSize();
	}
	
	@Test
	public void givenId_whenModify_thenReturnTodo() throws Exception{
		//given
		String modifyContent = "회의실 예약하기";
		String modifyCompYn = "Y";
		
		Todo todo = this.setUpTodo;
		todo.setContent(modifyContent);
		todo.setCompYn(modifyCompYn);
		
		//when
		Todo resultTodo = todoService.modify(todo);
		
		//then
		assertThat(resultTodo).isNotNull();
		assertThat(resultTodo.getContent()).isEqualTo(modifyContent);
		assertThat(resultTodo.getCompYn()).isEqualTo(modifyCompYn);
	}
	
	@Test
	public void givenId_whenRemoveAndFind_thenReturnNull() throws Exception{
		//given
		Long id = this.setUpTodo.getId();
		
		//when
		Todo resultTodo = todoService.find(id);
		assertThat(resultTodo).isNotNull();
		
		todoService.remove(id);
		
		resultTodo = todoService.find(id);
		
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
		
		List<RefTodo> refTodoList = new ArrayList<RefTodo>();
		
		RefTodo refTodo1 = new RefTodo();
		refTodo1.setTodoId(todo.getId());
		refTodo1.setRefTodoId(todoR1.getId());
		refTodoList.add(refTodo1);
		
		RefTodo refTodo2 = new RefTodo();
		refTodo2.setTodoId(todo.getId());
		refTodo2.setRefTodoId(todoR2.getId());
		refTodoList.add(refTodo2);
		
		refTodoService.registerList(refTodoList);
		
		List<RefTodo> resultSize2 = refTodoService.findAllByTodoId(todo.getId());
		
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
		Todo todo = new Todo();
		todo.setContent("비행기 예약하기");
		todo = todoService.register(todo);
		
		Todo todoR1 = new Todo();
		todoR1.setContent("호텔 예약하기");
		todoR1 = todoService.register(todoR1);
		
		RefTodo refTodo1 = new RefTodo();
		refTodo1.setTodoId(todo.getId());
		refTodo1.setRefTodoId(todoR1.getId());
		
		List<RefTodo> refTodoList = new ArrayList<RefTodo>();
		refTodoList.add(refTodo1);
		refTodoService.registerList(refTodoList);
		
		assertThat(todo).isNotNull();
		assertThat(todo.getId()).isNotNull();
		
		List<RefTodo> resultSize1 = refTodoService.findAllByTodoId(todo.getId());
		assertThat(resultSize1.size()).isEqualTo(1);
		
		//RefTodo 목록 삭제 후 재입력
		List<RefTodo> refTodoList2 = new ArrayList<RefTodo>();
		for(int i=0; i<90; i++) {
			Todo todoF = new Todo();
			todoF.setContent("todoF : " + i);
			todoService.register(todoF);
			
			RefTodo refTodoF = new RefTodo();
			refTodoF.setTodoId(todo.getId());
			refTodoF.setRefTodoId(todoF.getId());
			
			refTodoList2.add(refTodoF);
		}
		
		refTodoService.registerList(refTodoList2);
				
		//then
		assertThat(todo).isNotNull();
		assertThat(todo.getId()).isNotNull();
		
		List<RefTodo> resultRefTodosSize90 = refTodoService.findAllByTodoId(todo.getId());
		assertThat(resultRefTodosSize90.size()).isEqualTo(90);
	}
}
