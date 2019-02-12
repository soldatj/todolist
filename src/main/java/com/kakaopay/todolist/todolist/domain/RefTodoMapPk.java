package com.kakaopay.todolist.todolist.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RefTodoMapPk implements Serializable{
	private static final long serialVersionUID = -1754946376034089035L;
	private Long todoId;
	private Long refTodoId;
}