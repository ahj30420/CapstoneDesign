package hello.capstone.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

	DUPLICATED_USER_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
	PASSWORD_MISMATCH(HttpStatus.CONFLICT, "패스워드가 틀렸습니다."),
	DUPLICATED_SHOP(HttpStatus.CONFLICT, "해당 주소에 가게가 존재합니다."),
	NONEXISTENT_MEMBER(HttpStatus.CONFLICT, "해당 정보의 사용자가 존재하지 않습니다."),
	PHONE_MISMATCH(HttpStatus.CONFLICT, "해당 정보와 핸드폰 번호가 일치하지 않습니다."),
	Code_MISMATCH(HttpStatus.CONFLICT, "인증번호가 틀렸습니다."),
	ALREADY_BOOKMARKED_SHOP(HttpStatus.CONFLICT, "이미 즐겨찾기를 한 가게 입니다."),
	NICKNAME_DUPLICATED_OR_MORE_TAHN_15LETTERS(HttpStatus.CONFLICT, "기존 닉네임과 다른 15글자 이내에 닉네임을 입력하세요.");
	
	private HttpStatus status;
	private String message;
}