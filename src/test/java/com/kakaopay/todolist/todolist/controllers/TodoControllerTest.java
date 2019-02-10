package com.kakaopay.todolist.todolist.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kakaopay.todolist.todolist.domain.Result;
import com.kakaopay.todolist.todolist.domain.Todo;
import com.kakaopay.todolist.todolist.services.TodoService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerTest {
	@Autowired
	private TodoController todoController;
	
	@Autowired
	private TodoService todoService;
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	private Todo setUpTodo = null;
	
	@Before
    public void init() {
		Todo todo = new Todo();
		todo.setContent("호텔 예약하기");
		todo.setCompYn("N");
		
		Todo result = todoService.register(todo);
		assertThat(result).isNotNull();
		
		this.setUpTodo = result;
    }
	
	@Test
    public void givenId_whenFind_thenReturnTodo() throws Exception {
		//given
		Long id = this.setUpTodo.getId();
		
		//when
        ResponseEntity<Result> response = restTemplate.getForEntity("/api/todo/"+id, Result.class);
        
        //then
        assertThat(response.getBody()).isNotNull();
        
        Result resResult = response.getBody();
        assertThat(resResult.getErrorCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(resResult.getErrorMessage()).isEqualTo("success");
        
        
        Map resultMap = (Map)resResult.getResult();
        assertThat(resultMap).isNotNull();
		assertThat(String.valueOf(resultMap.get("id"))).isEqualTo(String.valueOf(id));
    }
}
