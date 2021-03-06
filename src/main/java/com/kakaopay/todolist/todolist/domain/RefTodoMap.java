package com.kakaopay.todolist.todolist.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="RefTodo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@IdClass(RefTodoMapPk.class)
public class RefTodoMap {
	@Id
	@Column
	private Long todoId;
	 
	@Id
	@Column
	private Long refTodoId;
}


