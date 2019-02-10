package com.kakaopay.todolist.todolist.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

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

	private Todo setUpTodo = null;
	
	@Before
	public void setup() throws Exception{
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
	}
	
	@Test
	public void whenRegister_thenReturnTodo() throws Exception{
		//when
		Todo todo = new Todo();
		todo.setContent("호텔 예약하기");
		
		Todo refTodo1 = new Todo();
		refTodo1.setId(4l);
		
		ArrayList<Todo> refTodoList = new ArrayList<Todo>();
		refTodoList.add(refTodo1);
		
		todo.setRefTodoList(refTodoList);
		
		Todo result = todoService.register(todo);
		
		//then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isNotNull();
	}
	
	@Test
	public void whenRegisterListAndFindAll_thenReturnTodoList() throws Exception{
		//when
		for(int i=0; i>100; i++) {
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
}
