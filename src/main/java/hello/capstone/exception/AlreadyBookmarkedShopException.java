package hello.capstone.exception;

import hello.capstone.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlreadyBookmarkedShopException extends RuntimeException{
    private ErrorCode errorCode;    
    private String message;         

    @Override
    public String toString() {
        if(message == null) {
            //  Service로부터 생성자를 통해 상태만 받아오고 message를 안받아오면 ErrorCode에 미리 설정해둔 Message를 출력한다.
            return errorCode.getMessage();
        }

        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}