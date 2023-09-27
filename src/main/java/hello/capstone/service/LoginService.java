package hello.capstone.service;



import java.sql.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.stereotype.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import hello.capstone.dto.Member;
import hello.capstone.exception.LogInException;
import hello.capstone.exception.SignUpException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {
	
	private final MemberRepository memberRepository;
	/*
	 * 회원가입 - 마지막 수정 09/20/ 23시 20분
	 * */
	public boolean signUp(Member member) {
		
		
		//.ifPresent()는 memberRepository.findById 실행 시 오류 던져주기 위함
		Optional.ofNullable(memberRepository.findById(member.getId(),"normal"))
			.ifPresent(user->{
				throw new SignUpException(ErrorCode.DUPLICATED_USER_ID,null);
			});
		
		long miliseconds = System.currentTimeMillis();
		Date redate = new Date(miliseconds);
		member.setNickname(createRandomNickname());
		member.setSocial("normal");
		member.setRedate(redate);
		
		return memberRepository.save(member);
    	
	}
	

	/*
	 * 카카오 회원가입 - 
	 * */
	public Member kakaoSignUp(Member member) {
		
		
		Member findMember = memberRepository.findById(member.getId(),"kakao");
		
		
		if(Objects.isNull(findMember)) {
			long miliseconds = System.currentTimeMillis();
			Date redate = new Date(miliseconds);
			
			String uuidPw = (UUID.randomUUID()).toString();
			
			member.setPw(uuidPw);
			member.setNickname(createRandomNickname());
			member.setSocial("kakao");
			member.setRole("사용자");
			member.setRedate(redate);
			
			return memberRepository.saveSocial(member);
		}
		return findMember;
    	
	}
	/*
	 * 카카오 회원가입 - 
	 * */
	public Member naverSignUp(Member member) {
		
		
		Member findMember = memberRepository.findById(member.getId(),"naver");
		
		
		if(Objects.isNull(findMember)) {
			long miliseconds = System.currentTimeMillis();
			Date redate = new Date(miliseconds);
			
			String uuidPw = (UUID.randomUUID()).toString();
			
			member.setPw(uuidPw);
			member.setNickname(createRandomNickname());
			member.setSocial("naver");
			member.setRole("사용자");
			member.setRedate(redate);
			
			
			log.info("member ={}", member);
			
			return memberRepository.saveSocial(member);
		}
		return findMember;
    	
	}
	
	
	/*
	 * 로그인 
	 */
	public Member login(String id, String pw) {
		
		
		Member userMember = memberRepository.findById(id,"normal");
		boolean pwCheck = passwordCheck(userMember, pw);

		return userMember;
	}
	
	
	/*
	 * 인증 메시지
	 */
	public SingleMessageSentResponse sendMessage(String phone, String code) {
		
		DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCS2WM9OPHXRSX3S", "HBXEEKMI2NVBRJV4L5HJSFVN31AITSDU", "https://api.coolsms.co.kr");
		
		Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01085864484");
        message.setTo(phone);
        message.setText("[재고30]인증번호 " + code + "를 입력하세요.");

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        log.info("sendMessageResponse={}", response);

        return response;
	}
	
/*
 *-----------------------------------------------------------------------------------------------------
 *private 메소드
 *----------------------------------------------------------------------------------------------------- 	
 */
	
	//비밀번호 일치 확인
	private boolean passwordCheck(Member userMember, String pw) {
		boolean pwCheck = true;
		if(!(userMember.getPw().equals(pw))) {
	    	  throw new LogInException(ErrorCode.PASSWORD_MISMATCH, null);
	      }
		
		return pwCheck;
	}
	
	//중복회원 검사
	private Member duplicateCheck(Member member) {
		Member findMember = memberRepository.findById(member.getId(),"normal");
		
		return findMember;
	}
	//닉네임 임의 random String
	private String createRandomNickname() {
		int randomNicknameLen = 8;
		boolean useLetters = true;
		boolean useNumbers = false;
		String randomNick = RandomStringUtils.random(randomNicknameLen, useLetters, useNumbers);
			
		log.info("create random nickname = {} ", randomNick);
			
		return randomNick;
	}
}