package com.kakaopay.todolist.todolist.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kakaopay.todolist.todolist.domain.Result;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerTest {
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void whenRegisterAndFind_thenReturnTodo() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		Map<String, String> map = new HashMap<>();
		String content = "호텔 예약하기";
		map.put("content", content);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<Result> registerResponse = restTemplate.postForEntity("/api/todo/", request, Result.class);
		assertThat(registerResponse).isNotNull();
		
		Integer todoId = (Integer)registerResponse.getBody().getResult();
		
		ResponseEntity<Result> findResponse = restTemplate.getForEntity("/api/todo/find/"+todoId, Result.class);
		assertThat(registerResponse).isNotNull();
		Map<String, String> todo = (Map<String, String>) findResponse.getBody().getResult();
		
		assertThat(String.valueOf(todo.get("id"))).isEqualTo(String.valueOf(todoId));
		assertThat(String.valueOf(todo.get("content"))).isEqualTo(content);
	}
	
	@Test
	public void whenModify_thenModifiedTodo() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		Map<String, String> map = new HashMap<>();
		String content = "호텔 예약하기";
		map.put("content", content);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<Result> registerResponse = restTemplate.postForEntity("/api/todo/", request, Result.class);
		assertThat(registerResponse).isNotNull();
		
		Integer todoId = (Integer)registerResponse.getBody().getResult();
		
		ResponseEntity<Result> findResponse = restTemplate.getForEntity("/api/todo/find/"+todoId, Result.class);
		assertThat(findResponse).isNotNull();
		Map<String, String> todo = (Map<String, String>) findResponse.getBody().getResult();
		
		//put을 통해 값을 수정
		String newContent = "영화 보기";
		todo.put("content", newContent);
		HttpEntity<Map<String, String>> modalRequest = new HttpEntity<>(todo, headers);
		restTemplate.put("/api/todo/modal/", modalRequest);
		
		ResponseEntity<Result> findResponse2 = restTemplate.getForEntity("/api/todo/find/"+todoId, Result.class);
		assertThat(findResponse2).isNotNull();
		Map<String, String> todo2 = (Map<String, String>) findResponse2.getBody().getResult();
		
		assertThat(String.valueOf(todo2.get("id"))).isEqualTo(String.valueOf(todoId));
		assertThat(String.valueOf(todo2.get("content"))).isEqualTo(newContent);
	}
	
	@Test
	public void whenRegisterTurnCompleteStat_thenCompYnToggledTodo() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		Map<String, String> map = new HashMap<>();
		String content = "호텔 예약하기";
		map.put("content", content);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<Result> registerResponse = restTemplate.postForEntity("/api/todo/", request, Result.class);
		assertThat(registerResponse).isNotNull();
		
		Integer todoId = (Integer)registerResponse.getBody().getResult();
		
		//complte 값 Y로 전환
		restTemplate.put("/api/todo/complete/"+todoId, null);
		
		ResponseEntity<Result> findResponse2 = restTemplate.getForEntity("/api/todo/find/"+todoId, Result.class);
		assertThat(findResponse2).isNotNull();
		Map<String, String> todo2 = (Map<String, String>) findResponse2.getBody().getResult();
		
		assertThat(String.valueOf(todo2.get("id"))).isEqualTo(String.valueOf(todoId));
		assertThat(String.valueOf(todo2.get("compYn"))).isEqualTo("Y");
		
		
		//complte 값 N으로 전환
		restTemplate.put("/api/todo/cancel/"+todoId, null);
		
		ResponseEntity<Result> findResponse3 = restTemplate.getForEntity("/api/todo/find/"+todoId, Result.class);
		assertThat(findResponse3).isNotNull();
		Map<String, String> todo3 = (Map<String, String>) findResponse3.getBody().getResult();
		
		assertThat(String.valueOf(todo3.get("id"))).isEqualTo(String.valueOf(todoId));
		assertThat(String.valueOf(todo3.get("compYn"))).isEqualTo("N");
	}
}
