package com.kakaopay.todolist.todolist.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class ExistNotCompleteRefTodoException extends RuntimeException {
	private static final long serialVersionUID = 8339721780907205226L;
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message = "완료되지 않은 참조 할일이 존재합니다.";
    
    public ExistNotCompleteRefTodoException(Long todoId) {
    	this.message = new StringBuffer()
    			.append("[").append(todoId).append("]")
    			.append(this.message)
    			.toString();
    }
}
