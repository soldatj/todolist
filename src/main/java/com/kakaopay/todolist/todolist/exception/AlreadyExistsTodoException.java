package com.kakaopay.todolist.todolist.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AlreadyExistsTodoException extends RuntimeException {
	private static final long serialVersionUID = -7020270854301001489L;
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message = "이미 존재하는 할일입니다.";
    
}
