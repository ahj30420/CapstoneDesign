package hello.capstone.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.capstone.dto.Member;
import hello.capstone.exception.CodeVerificationException;
import hello.capstone.exception.SendMessageException;
import hello.capstone.exception.ValidationException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.service.LoginService;
import hello.capstone.service.MemberService;
import hello.capstone.validation.group.SignUpValidationGroup;
import hello.capstone.validation.group.UpdatePwValidationGroup;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
	
	private final LoginService loginService;
	private final MemberService memberService;
	private final PasswordEncoder bCryptPasswordEncoder;
	private final MessageSource messageSource;

	/*
	 * 일반 회원 회원가입
	 */
    @PostMapping("/join")
    public String signUp(@Validated(value = SignUpValidationGroup.class) @RequestBody Member member, BindingResult bindingResult){
    	
    	if(bindingResult.hasErrors()) {
    		sendErrors(bindingResult);
    	}
    	
    	String pw = bCryptPasswordEncoder.encode(member.getPw());
    	member.setPw(pw);
    	
    	loginService.signUp(member);
    	
		return "/login";
    }
    
    /*
     * 관리자 페이지 로그인
     */
    @PostMapping("/admin/login")
    public String adminLogin(@RequestBody HashMap<String, Object> loginMap, HttpServletRequest request) {
       String id = (String) loginMap.get("id");
       String pw = (String) loginMap.get("pw");
       
       if(id.isEmpty()) {
          return "/ad_login";
       }
       Member member = loginService.adminLogin(id, pw);
       HttpSession session = request.getSession();       
       
       session.setAttribute("admin", member);
       
       return "/ad_user";
    }
    
    /*
     * 일반 회원 로그인
     */
    @PostMapping("/login")
    public String login(@RequestBody HashMap<String, Object> loginMap, HttpServletRequest request) {
    	String id = (String) loginMap.get("id");
    	String pw = (String) loginMap.get("pw");
		
    	if(id.isEmpty()) {
    		return "/login";
    	}
    	Member userMember = loginService.login(id, pw);
    	HttpSession session = request.getSession();
    	
    	log.info("id= {}", id);
    	session.setAttribute("member", userMember);
    	
    	
    	return "home_user";
    }
    
    
    @GetMapping("/getSessionMember")
    public Member getSessionMember(HttpSession session) {
    	Member member = (Member)session.getAttribute("member");
    	
    	return member; 
    	
    }
    @GetMapping("/getSessionMember/business")
    public Member getSessionMemberBusiness(HttpSession session) {
    	Member member = (Member)session.getAttribute("member");
    	
    	return member; 
    }
    @GetMapping("/getSessionMember/manager")
    public Member getSessionMemberManager(HttpSession session) {
    	Member member = (Member)session.getAttribute("admin");
    	
    	return member; 
    }
    
    @GetMapping("/SessionLogout")
    public void SessionLogout(HttpSession session) {
    	
    	session.removeAttribute("member");
    	log.info("로그아웃 성공. 현재 멤버 세션 = {}", session.getAttribute("member"));
    }
    
    /*
     * 아아디, 비밀번호 찾기
     */
    //아이디 확인(비밀번호 찾기)
    @GetMapping("/id-verification")
    public String idVerification(@RequestParam String id, HttpServletRequest request) {
    	Member member = memberService.IdVerification(id);
    	HttpSession session = request.getSession();
    	
    	session.setAttribute("findpw_member", member);
    	return "ok";
    }
    
    /*
     * 비밀번호 찾기(인증 문자 전송)
     */
    @GetMapping("/findpw-sendmessage")
    public SingleMessageSentResponse findPwSendMessage(@RequestParam String phone, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	Member member = (Member)session.getAttribute("findpw_member");
    	
    	if(phone.equals(member.getPhone())) {
    		return Message(phone, request);
    	}
    	else {
    		throw new SendMessageException(ErrorCode.PHONE_MISMATCH,null);
    	}
    	
}
    
    /*
     * 아이디 찾기(인증 문자 전송)
     */
    @GetMapping("/findid-sendmessage")
    public SingleMessageSentResponse findIdSendMessage(@RequestParam(value = "name", required = false) String name,@RequestParam("phone") String phone, HttpServletRequest request) {
    	HttpSession session = request.getSession();

    	if(name != null) {
    		Member member2 = memberService.nameVerification(name,phone);
    		session.setAttribute("findid_member", member2);
    		
    		if(phone.equals(member2.getPhone())) {
    			return Message(phone,request);
    		}
    		else {
    			throw new SendMessageException(ErrorCode.PHONE_MISMATCH,null);
    		}
    	}
    	else {
    		throw new SendMessageException(ErrorCode.PHONE_MISMATCH,null);
    	}
    } 
    
    /*
     * 아이디 찾기(이름, 휴대폰 인증 성공 후 실제로 아이디 정보 보여주기)
     */
    @GetMapping("/find-id")
    public String showID(HttpServletRequest request) {
       HttpSession session = request.getSession();
       Member findMember = (Member)session.getAttribute("findid_member");
       String id = findMember.getId();
       
       session.removeAttribute("findid_member");
       return id;
    }
    
    
    /*
     * 비밀번호 찾기(인증 문자 확인)
     */
    @GetMapping("pw-code-verification")
    public String pwCodeVerification(@RequestParam String code, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	if(code.equals(session.getAttribute("code")) && session.getAttribute("findpw_member") != null) {
    		session.removeAttribute("code");
    		return "success";
    	}
    	else {
    		throw new CodeVerificationException(ErrorCode.Code_MISMATCH,null);
    	}
    }
    
    /*
     * 아이디 찾기(인증 문자 확인)
     */
    @GetMapping("id-code-verification")
    public String IdCodeVerification(@RequestParam String code, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	if(code.equals(session.getAttribute("code")) && session.getAttribute("findid_member") != null) {
    		session.removeAttribute("code");
    		return "success";
    	}
    	else {
    		throw new CodeVerificationException(ErrorCode.Code_MISMATCH,null);
    	}
    }
    
    @PutMapping("/updatepw")
    public String updatePw(@Validated(value = UpdatePwValidationGroup.class) @RequestBody Member memberPw, BindingResult bindingResult, HttpServletRequest request) {
    	//변경 비밀번호 검증 및 암호화
    	if(bindingResult.hasErrors()) {
    		sendErrors(bindingResult);
    	}
    	
    	HttpSession session = request.getSession();
    	Member member = (Member)session.getAttribute("findpw_member");
    	
    	//암호화
    	String pw = bCryptPasswordEncoder.encode(memberPw.getPw());
    	
    	memberService.updatePw(member.getId(),pw);
    	session.removeAttribute("find_member");
    	
    	return "/login";
    }
    
    //-------------------------------------------------------------------------------------------------------
    
    
    //검증 오류
    private void sendErrors(BindingResult bindingResult) {
 	   Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
     	   String em = messageSource.getMessage(error, Locale.getDefault());
           errors.put(error.getField(), em);
        }
        throw new ValidationException(errors);
    }
    
    
    //인증 번호 문자 보내는 코드
    private SingleMessageSentResponse Message(String phone, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	String code = RandomStringUtils.randomNumeric(6);
		session.setAttribute("code", code);
		SingleMessageSentResponse reponse = loginService.sendMessage(phone,code);
    	return reponse;
    }
    
}