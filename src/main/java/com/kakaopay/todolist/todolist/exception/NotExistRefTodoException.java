package com.kakaopay.todolist.todolist.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class NotExistRefTodoException extends RuntimeException {
	private static final long serialVersionUID = 5194136849053062443L;
	private HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private String message = "참조되는 할일이 존재하지 않습니다.";
    
    public NotExistRefTodoException(Long todoId) {
    	this.message = new StringBuffer()
    			.append("[").append(todoId).append("] ")
    			.append(this.message)
    			.toString();
    }
}
