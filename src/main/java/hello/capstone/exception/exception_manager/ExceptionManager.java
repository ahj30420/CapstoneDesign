package hello.capstone.exception.exception_manager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import hello.capstone.dto.Response;
import hello.capstone.exception.LogInException;
import hello.capstone.exception.SaveShopException;
import hello.capstone.exception.SignUpException;

//예외처리하게 되면 해당 예외에 맞는 기능이 동작됨
//유저는 어떤 에러가 발생한지 모르기 때문에 여기서 예외처리에 맞는 에러 값을 유저에게 알려주는 공간
@RestControllerAdvice
public class ExceptionManager {

	// (1) 모든 RuntimeException 에러가 발생시 동작
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e){   // ? 는 모든 값이 올 수 있다는 것
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)      // 서버 에러 상태 메시지와 body에 에러상태 메시지(문자열)을 넣어 반환해줌
	            .body(Response.error(e.getMessage(), null));
	}
	
	// (2) 기존에 만들어둔 에러(SignUpException)가 발생시 동작
	@ExceptionHandler(SignUpException.class)
	public ResponseEntity<?> SignUpExceptionHandler(SignUpException e){
	    return ResponseEntity.status(e.getErrorCode().getStatus())
	            .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
 
	//(3) 기존에 만들어둔 에러(LogInException)가 발생시 동작
	@ExceptionHandler(LogInException.class)
	public ResponseEntity<?> LogInExceptionHandler(LogInException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}
	
	//(4) 기존에 만들어둔 에러(SaveShopException)가 발생시 동작
	@ExceptionHandler(SaveShopException.class)
	public ResponseEntity<?> SaveShopExceptionHandler(SaveShopException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}
}