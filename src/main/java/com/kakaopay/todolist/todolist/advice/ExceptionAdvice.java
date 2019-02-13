package com.kakaopay.todolist.todolist.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.kakaopay.todolist.todolist.domain.Result;
import com.kakaopay.todolist.todolist.exception.AlreadyExistsTodoException;
import com.kakaopay.todolist.todolist.exception.ExistCompleteRefToMeTodoException;
import com.kakaopay.todolist.todolist.exception.ExistNotCompleteRefTodoException;
import com.kakaopay.todolist.todolist.exception.NotExistRefTodoException;
import com.kakaopay.todolist.todolist.exception.NotExistTodoException;
import com.kakaopay.todolist.todolist.exception.NotExistTodoRefDataException;

@ControllerAdvice
public class ExceptionAdvice {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyExistsTodoException.class)
    @ResponseBody
    public Result handleAlreadyExistsTodoException(HttpServletRequest req, AlreadyExistsTodoException ex) {
        return new Result(ex.getHttpStatus().value(), ex.getMessage());
    }
	
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotExistTodoException.class)
    @ResponseBody
    public Result handleNotExistTodoException(HttpServletRequest req, NotExistTodoException ex) {
        return new Result(ex.getHttpStatus().value(), ex.getMessage());
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotExistTodoRefDataException.class)
    @ResponseBody
    public Result handleNotExistTodoRefDataException(HttpServletRequest req, NotExistTodoRefDataException ex) {
        return new Result(ex.getHttpStatus().value(), ex.getMessage());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExistNotCompleteRefTodoException.class)
    @ResponseBody
    public Result handleExistNotCompleteRefTodoException(HttpServletRequest req, ExistNotCompleteRefTodoException ex) {
        return new Result(ex.getHttpStatus().value(), ex.getMessage());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExistCompleteRefToMeTodoException.class)
    @ResponseBody
    public Result handleExistNotCompleteRefToMeTodoException(HttpServletRequest req, ExistCompleteRefToMeTodoException ex) {
        return new Result(ex.getHttpStatus().value(), ex.getMessage());
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotExistRefTodoException.class)
    @ResponseBody
    public Result handleNotExistRefTodoException(HttpServletRequest req, NotExistRefTodoException ex) {
        return new Result(ex.getHttpStatus().value(), ex.getMessage());
    }
    
    
    

}
