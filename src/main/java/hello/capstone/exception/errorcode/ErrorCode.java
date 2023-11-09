package hello.capstone.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

	DUPLICATED_USER_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
	NULL_USER_ID(HttpStatus.NOT_FOUND, "존재하지 않은 아이디입니다."),
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "패스워드가 틀렸습니다."),
	DUPLICATED_SHOP(HttpStatus.CONFLICT, "해당 주소에 가게가 존재합니다."),
	ALREADY_BOOKMARKED_SHOP(HttpStatus.CONFLICT, "이미 즐겨찾기를 한 가게 입니다."),
	NICKNAME_MORE_TAHN_15LETTERS(HttpStatus.UNPROCESSABLE_ENTITY, "닉네임은 최대 15자까지 입력가능합니다."),
	NONEXISTENT_MEMBER(HttpStatus.NOT_FOUND, "해당 정보의 사용자가 존재하지 않습니다."),
	PHONE_MISMATCH(HttpStatus.BAD_REQUEST, "해당 정보와 핸드폰 번호가 일치하지 않습니다."),
	Code_MISMATCH(HttpStatus.BAD_REQUEST, "인증번호가 틀렸습니다."),
	DUPLICATED_ITEM(HttpStatus.CONFLICT, "이미 등록된 상품입니다."),
	INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "휴대폰 번호는 '-'을 제외한 11자이어야 합니다."),
	INVALID_EMAIL_ID(HttpStatus.BAD_REQUEST, "올바르지 않은 이메일형식의 아이디입니다."),
	TIME_SETTING_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "이벤트 종료시간이 현재시간보다 이릅니다."),
	NULL_TITLE(HttpStatus.UNPROCESSABLE_ENTITY, "제목을 입력하세요."),
	NULL_CONTENT(HttpStatus.UNPROCESSABLE_ENTITY, "내용을 입력하세요."),
	EXCESS_QUANTITY(HttpStatus.CONFLICT, "예약하고자하는 양이 남아있는 수량을 초과하였습니다"),
	EXIST_ANSWER(HttpStatus.CONFLICT, "답변이 존재하여 더 이상 수정 할 수 없습니다."),
	NULL_PHONE(HttpStatus.UNPROCESSABLE_ENTITY, "핸드폰 번호를 입력하세요."),
	EXIST_RESERVATION_PERSON(HttpStatus.UNPROCESSABLE_ENTITY, "해당 상품을 예약한 사용자가 존재합니다. \n예약자 명단을 확인 후 상품을 삭제해주세요"),
	NOT_ADMINISTER(HttpStatus.FORBIDDEN, "해당 아이디로는 로그인 할 수 없습니다. 관리자 아이디로 로그인 해주세요.");
	
	private HttpStatus status;
	private String message;
}