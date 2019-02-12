package com.kakaopay.todolist.todolist.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
	
	TODO_NOT_FOUND("TODO_NOT_FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 할일 입니다."),
	TODO_EXISTS_NOT_COMPLETE_REF_TODOS("TODO_EXISTS_NOT_COMPLETE_REF_TODOS", HttpStatus.BAD_REQUEST, "완료되지 않은 참조 할일이 존재합니다."),
	TODO_UNKNOWN_ERROR("TODO_UNKNOWN_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "알수없는 오류 발생");
	
	private final String code;
	private final HttpStatus httpStatus;
	private final String message;

	public static ErrorCode getByCode(final String code) {
		for (final ErrorCode e : values()) {
			if (e.code.equals(code))
				return e;
		}
		return ErrorCode.TODO_UNKNOWN_ERROR;
	}

}
