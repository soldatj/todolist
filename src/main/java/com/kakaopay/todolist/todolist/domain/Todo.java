package com.kakaopay.todolist.todolist.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
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
@Table(name="Todo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Todo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;
	
	@NonNull
	@Column
	private String content;
	
	@Column
	private String compYn = "N";
	
	
//	@ManyToMany(cascade= {CascadeType.ALL}) 
//	@JoinTable( name = "RefTodo", 
//	joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
//	inverseJoinColumns = {@JoinColumn(name = "refId", referencedColumnName = "id")})
//	private List<Todo> todoList;
	
	
//	@JoinColumn(name="id", insertable = true)
//	@OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name="id")
//	@JoinColumn(name = "id", referencedColumnName = "todoId")
	  @OneToMany(cascade = CascadeType.ALL)
	  @JoinTable(name = "RefTodo",
	    joinColumns = @JoinColumn(name = "id"),
	    inverseJoinColumns = @JoinColumn(name = "todoId"))
	
	private List<RefTodo> refTodoList;
	
//	@OneToMany
//	@JoinTable(name="RefTodo",
//		joinColumns = @JoinColumn(name = "id"),
//		inverseJoinColumns = @JoinColumn(name = "refId"))
//	private List<Todo> refTodoList = new ArrayList<Todo>();
		
	@NonNull
	@CreationTimestamp
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date insDtm;
	
	@UpdateTimestamp
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date updDtm;
	
}
