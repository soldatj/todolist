package com.kakaopay.todolist.todolist.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class CrossReferenceException extends RuntimeException {
	private static final long serialVersionUID = -3043729264605321970L;
	
	private HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private String message = "할일은 상호간 참조가 불가능합니다.";
    
    public CrossReferenceException(Long todoId1, Long todoId2) {
    	this.message = new StringBuffer()
    			.append("[").append(todoId1).append(",").append(todoId2).append("] ")
    			.append(this.message)
    			.toString();
    }
}
