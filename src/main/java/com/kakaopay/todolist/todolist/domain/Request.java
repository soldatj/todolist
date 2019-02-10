package com.kakaopay.todolist.todolist.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Request {
	public Todo todo;
	
	public List<RefTodo> refTodoList;
}
