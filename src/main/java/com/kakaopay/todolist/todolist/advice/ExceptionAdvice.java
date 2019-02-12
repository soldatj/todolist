package com.kakaopay.todolist.todolist.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.kakaopay.todolist.todolist.common.ErrorCode;
import com.kakaopay.todolist.todolist.domain.Result;
import com.kakaopay.todolist.todolist.exception.ExistsNotCompleteRefTodosException;
import com.kakaopay.todolist.todolist.exception.TodoException;

@ControllerAdvice
public class ExceptionAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ExistsNotCompleteRefTodosException.class)
    @ResponseBody
    public Result handleExistsNotCompleteRefTodos(HttpServletRequest req, ExistsNotCompleteRefTodosException ex) {
        return new Result(ex.getHttpStatus().value(), ex.getMessage());
    }

}
