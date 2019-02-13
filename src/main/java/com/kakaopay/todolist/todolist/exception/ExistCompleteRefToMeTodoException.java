package com.kakaopay.todolist.todolist.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class ExistCompleteRefToMeTodoException extends RuntimeException {
	private static final long serialVersionUID = 4604258247151841552L;
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message = "완료된 참조 할일이 존재합니다.";
    
    public ExistCompleteRefToMeTodoException(Long todoId) {
    	this.message = new StringBuffer()
    			.append("[").append(todoId).append("] ")
    			.append(this.message)
    			.toString();
    }
}
