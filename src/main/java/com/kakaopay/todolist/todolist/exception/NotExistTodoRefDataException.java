package com.kakaopay.todolist.todolist.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class NotExistTodoRefDataException extends RuntimeException {
	private static final long serialVersionUID = -3043729264605321970L;
	
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message = "존재하지 않는 참조 할일이 존재합니다.";
}
