package hello.capstone.exception.exception_manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import hello.capstone.dto.Response;
import hello.capstone.exception.LogInException;
import hello.capstone.exception.NicknameException;
import hello.capstone.exception.NullContentException;
import hello.capstone.exception.NullPhoneException;
import hello.capstone.exception.NullTitleException;
import hello.capstone.exception.QuantityException;
import hello.capstone.exception.SaveItemException;
import hello.capstone.exception.SaveShopException;
import hello.capstone.exception.SendMessageException;
//import hello.capstone.exception.SendMessageException;
import hello.capstone.exception.SignUpException;
import hello.capstone.exception.TimeSettingException;
import hello.capstone.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import hello.capstone.exception.AdminLoginException;
import hello.capstone.exception.AlreadyBookmarkedShopException;
import hello.capstone.exception.CodeVerificationException;
import hello.capstone.exception.ExistReservationException;
import hello.capstone.exception.FindPwException;
import hello.capstone.exception.InquiryException;
import hello.capstone.exception.InvalidEmailException;
import hello.capstone.exception.InvalidPhoneNumberException;
//import hello.capstone.exception.CodeVerificationException;
//import hello.capstone.exception.FindPwException;
//예외처리하게 되면 해당 예외에 맞는 기능이 동작됨
//유저는 어떤 에러가 발생한지 모르기 때문에 여기서 예외처리에 맞는 에러 값을 유저에게 알려주는 공간
@RestControllerAdvice
public class ExceptionManager {

	//모든 RuntimeException 에러가 발생시 동작
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e){   // ? 는 모든 값이 올 수 있다는 것
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)      // 서버 에러 상태 메시지와 body에 에러상태 메시지(문자열)을 넣어 반환해줌
	            .body(Response.error(e.getMessage(), null));
	}
	
	//기존에 만들어둔 에러(SignUpException)가 발생시 동작
	@ExceptionHandler(SignUpException.class)
	public ResponseEntity<?> SignUpExceptionHandler(SignUpException e){
	    return ResponseEntity.status(e.getErrorCode().getStatus())
	            .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
 
	//기존에 만들어둔 에러(LogInException)가 발생시 동작
	@ExceptionHandler(LogInException.class)
	public ResponseEntity<?> LogInExceptionHandler(LogInException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	
	//기존에 만들어둔 에러(SaveShopException)가 발생시 동작
	@ExceptionHandler(SaveShopException.class)
	public ResponseEntity<?> SaveShopExceptionHandler(SaveShopException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	
	//기존에 만들어둔 에러(AlreadyBookmarkedShopException)가 발생시 동작
	@ExceptionHandler(AlreadyBookmarkedShopException.class)
	public ResponseEntity<?> AlreadyBookmarkedShopExceptionHandler(AlreadyBookmarkedShopException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	
	//기존에 만들어둔 에러(NicknameException)가 발생시 동작
	@ExceptionHandler(NicknameException.class)
	public ResponseEntity<?> NicknameExceptionHandler(NicknameException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(FindPwException)가 발생시 동작
	@ExceptionHandler(FindPwException.class)
	public ResponseEntity<?> FindPwExceptionHandler(FindPwException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
		
	//기존에 만들어둔 에러(SendMessageException)가 발생시 동작
	@ExceptionHandler(SendMessageException.class)
	public ResponseEntity<?> SendMessageExceptionHandler(SendMessageException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(CodeVerificationException)가 발생시 동작
	@ExceptionHandler(CodeVerificationException.class)
	public ResponseEntity<?> CodeVerificationExceptionHandler(CodeVerificationException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(SaveItemException)가 발생시 동작
	@ExceptionHandler(SaveItemException.class)
	public ResponseEntity<?> SaveItemExceptionHandler(SaveItemException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(TimeSettingException)가 발생시 동작
	@ExceptionHandler(TimeSettingException.class)
	public ResponseEntity<?> TimeSettingExceptionHandler(TimeSettingException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(InvalidPhoneNumberException)가 발생시 동작
	@ExceptionHandler(InvalidPhoneNumberException.class)
	public ResponseEntity<?> InvalidPhoneNumberExceptionHandler(InvalidPhoneNumberException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(InvalidEmailException)가 발생시 동작
	@ExceptionHandler(InvalidEmailException.class)
	public ResponseEntity<?> InvalidEmailExceptionHandler(InvalidEmailException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().getMessage(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(QuantityException)가 발생시 동작
	@ExceptionHandler(QuantityException.class)
	public ResponseEntity<?> InvalidEmailExceptionHandler(QuantityException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}	
	//기존에 만들어둔 에러(InquiryException)가 발생시 동작
	@ExceptionHandler(InquiryException.class)
	public ResponseEntity<?> InquiryExceptionHandler(InquiryException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(NullTitleException)가 발생시 동작
	@ExceptionHandler(NullTitleException.class)
	public ResponseEntity<?> NullTitleExceptionHandler(NullTitleException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(NullContentException)가 발생시 동작
	@ExceptionHandler(NullContentException.class)
	public ResponseEntity<?> NullContentExceptionHandler(NullContentException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(NullPhoneException)가 발생시 동작
	@ExceptionHandler(NullPhoneException.class)
	public ResponseEntity<?> NullPhoneExceptionHandler(NullPhoneException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(ExistReservationException)가 발생시 동작
	@ExceptionHandler(ExistReservationException.class)
	public ResponseEntity<?> ExistReservationExceptionHandler(ExistReservationException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
	//기존에 만들어둔 에러(ValidationException)가 발생시 동작
	@ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrors());
    }
	//기존에 만들어둔 에러(AdminLoginException)가 발생시 동작
	@ExceptionHandler(AdminLoginException.class)
	public ResponseEntity<?> AdminLoginExceptionHandler(AdminLoginException e){
	   return ResponseEntity.status(e.getErrorCode().getStatus())
	           .body(Response.error(e.getErrorCode().name(),e.getErrorCode().getMessage()));
	}
}