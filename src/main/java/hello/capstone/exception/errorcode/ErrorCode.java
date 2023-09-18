package hello.capstone.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

	DUPLICATED_USER_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다.");
	
	private HttpStatus status;
	private String message;
}
