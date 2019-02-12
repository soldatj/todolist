package com.kakaopay.todolist.todolist.exception;

import org.springframework.http.HttpStatus;

import com.kakaopay.todolist.todolist.common.ErrorCode;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class TodoException extends RuntimeException {

    private ErrorCode code;
    private HttpStatus httpStatus;

    public TodoException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

}
