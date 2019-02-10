package com.kakaopay.todolist.todolist.domain;

import java.io.Serializable;

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
@IdClass(RefTodoPk.class)
public class RefTodo {
	@Id
	@Column
	private Long id;
	 
	@Id
	@Column
	private Long refId;
}

class RefTodoPk implements Serializable{
	private Long id;
	private Long refId;
}
