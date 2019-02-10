package com.kakaopay.todolist.todolist.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Data
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
	
	@NonNull
	@CreationTimestamp
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date insDtm;
	
	@UpdateTimestamp
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date updDtm;
}

class RefTodoPk implements Serializable{
	private Long id;
	
	private Long refId;
}
