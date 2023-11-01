package hello.capstone.exception;

import hello.capstone.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidEmailException extends RuntimeException{

	private final ErrorCode errorCode;
	private String message;
	
	@Override
	public String toString() {
		if(message == null) {
			return errorCode.getMessage();
		}
		
		return String.format("%s. %s", errorCode.getMessage(), message);
	}
	
}
