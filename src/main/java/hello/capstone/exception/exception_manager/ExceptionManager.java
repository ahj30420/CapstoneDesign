package hello.capstone.exception.exception_manager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import hello.capstone.dto.Response;
import hello.capstone.exception.*;

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
	
	//(5) 기존에 만들어둔 에러(FindPwException)가 발생시 동작
	@ExceptionHandler(FindPwException.class)
	public ResponseEntity<?> FindPwExceptionHandler(FindPwException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}
	
	//(6) 기존에 만들어둔 에러(SendMessageException)가 발생시 동작
	@ExceptionHandler(SendMessageException.class)
	public ResponseEntity<?> SendMessageExceptionHandler(SendMessageException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}
	
	//(7) 기존에 만들어둔 에러(AlreadyBookmarkedShopException)가 발생시 동작
	@ExceptionHandler(AlreadyBookmarkedShopException.class)
	public ResponseEntity<?> AlreadyBookmarkedShopExceptionHandler(AlreadyBookmarkedShopException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}
	
	//(8) 기존에 만들어둔 에러(NicknameException)가 발생시 동작
	@ExceptionHandler(NicknameException.class)
	public ResponseEntity<?> NicknameExceptionHandler(NicknameException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}
	
	//(9) 기존에 만들어둔 에러(CodeVerificationException)가 발생시 동작
	@ExceptionHandler(CodeVerificationException.class)
	public ResponseEntity<?> CodeVerificationExceptionHandler(CodeVerificationException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}

	//(10) 기존에 만들어둔 에러(SaveItemException)가 발생시 동작
	@ExceptionHandler(SaveItemException.class)
	public ResponseEntity<?> SaveItemExceptionHandler(SaveItemException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}
	
	//(11) 기존에 만들어둔 에러(TimeSettingException)가 발생시 동작
	@ExceptionHandler(TimeSettingException.class)
	public ResponseEntity<?> TimeSettingExceptionHandler(TimeSettingException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}
	
	//(12) 기존에 만들어둔 에러(TimeSettingException)가 발생시 동작
	@ExceptionHandler(InvalidPhoneNumberException.class)
	public ResponseEntity<?> InvalidPhoneNumberExceptionHandler(InvalidPhoneNumberException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}
	
	//(13) 기존에 만들어둔 에러(TimeSettingException)가 발생시 동작
	@ExceptionHandler(InvalidEmailException.class)
	public ResponseEntity<?> InvalidEmailExceptionHandler(InvalidEmailException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}	
}