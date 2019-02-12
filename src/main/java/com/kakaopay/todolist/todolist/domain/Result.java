package com.kakaopay.todolist.todolist.domain;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Result<T> {
	public Result(int errorCode, java.lang.String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	private int errorCode = HttpStatus.OK.value();
	
	private String errorMessage = "success";
	
	private T result;
}
