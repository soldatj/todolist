package com.kakaopay.todolist.todolist.exception;

import org.springframework.http.HttpStatus;

import com.kakaopay.todolist.todolist.common.ErrorCode;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class ExistsNotCompleteRefTodosException extends RuntimeException {
	private static final long serialVersionUID = -3043729264605321970L;
	
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message = "완료되지 않은 참조 할일이 존재합니다.";
}
