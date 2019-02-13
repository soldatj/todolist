package com.kakaopay.todolist.todolist.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class NotExistTodoException extends RuntimeException {
	private static final long serialVersionUID = 3081882788643804838L;
	private HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private String message = "할일이 존재하지 않습니다.";
    
    public NotExistTodoException(Long todoId) {
    	this.message = new StringBuffer()
    			.append("[").append(todoId).append("] ")
    			.append(this.message)
    			.toString();
    }
}
