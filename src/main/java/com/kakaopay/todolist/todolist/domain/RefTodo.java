package com.kakaopay.todolist.todolist.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@ManyToOne
	@JoinColumn(name="id", nullable=true)
	private Long id;
	 
	@Id
	@ManyToOne
	@JoinColumn(name="refId", nullable=true)
	private Long refId;
}

class RefTodoPk implements Serializable{
	private Long id;
	private Long refId;
}
