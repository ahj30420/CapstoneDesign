package hello.capstone.controller;

import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.capstone.dto.Member;
import hello.capstone.exception.CodeVerificationException;
import hello.capstone.exception.SendMessageException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.service.LoginService;
import hello.capstone.service.MemberService;
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
	/*
	 * 일반 회원 회원가입
	 */
    @PostMapping("/join")
    public String signUp(@RequestBody Member member){
    	
    	log.info("id ={}",member.getId());
    	log.info("pw={}",member.getPw());
    	log.info("name ={}",member.getName());
    	log.info("phone={}",member.getPhone());
    	log.info("role={}",member.getRole());
    	
    	boolean success = loginService.signUp(member);
    	
    	
    	log.info("SignUp Success !");
		return "/login";
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
    	
    	
    	
    	session.setAttribute("member", userMember);
    	
    	log.info("loginId={}",userMember.getId());
    	log.info("loginName={}",userMember.getName());
    	
    	return "home_user";
    }
    
    
    @GetMapping("/getSessionMember")
    public Member getSessionMember(HttpSession session) {
    	
    	log.info("member = {}", (Member)session.getAttribute("member"));
    	return (Member)session.getAttribute("member");
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
    @GetMapping("/ID_verification")
    public String ID_verification(@RequestParam String id, HttpServletRequest request) {
    	Member member = memberService.ID_verification(id);
    	HttpSession session = request.getSession();
    	session.setAttribute("findpw_member", member);
    	return "ok";
    }
    
    /*
     * 비밀번호 찾기(인증 문자 전송)
     */
    @GetMapping("/findPw_sendMessage")
    public SingleMessageSentResponse findPw_sendMessage(@RequestParam String phone, HttpServletRequest request) {
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
    @GetMapping("/findId_sendMessage")
    public SingleMessageSentResponse findId_sendMessage(@RequestParam(required = false) String name,@RequestParam String phone, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	if(name != null) {
    		Member member2 = memberService.Name_verification(name,phone);
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
     * 비밀번호 찾기(인증 문자 확인)
     */
    @GetMapping("Pw_Code_verification")
    public String Pw_Code_verification(@RequestParam String code, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	if(code.equals(session.getAttribute("code")) && session.getAttribute("findpw_member") != null) {
    		session.removeAttribute("code");
    		return "/pw_result";
    	}
    	else {
    		throw new CodeVerificationException(ErrorCode.Code_MISMATCH,null);
    	}
    }
    
    /*
     * 아이디 찾기(인증 문자 확인)
     */
    @GetMapping("Id_Code_verification")
    public String Id_Code_verification(@RequestParam String code, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	if(code.equals(session.getAttribute("code")) && session.getAttribute("findid_member") != null) {
    		session.removeAttribute("code");
    		return "success";
    	}
    	else {
    		throw new CodeVerificationException(ErrorCode.Code_MISMATCH,null);
    	}
    }
    
    /*
     * 비밀번호 새 설정
     */
    @PutMapping("/updatepw")
    public String updatepw(@RequestBody HashMap<String,String> pw, HttpServletRequest request) {
    	String password = pw.get("pw");
    	HttpSession session = request.getSession();
    	Member member = (Member)session.getAttribute("findpw_member");
    	memberService.updatepw(member.getId(),password);
    	session.removeAttribute("findpw_member");
    	return "/login";
    }
    
    /*
     * 아이디 찾기(이름, 휴대폰 인증 성공 후 실제로 아이디 정보 보여주기)
     */
    @GetMapping("/find_id")
    public String showID(HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	Member find_member = (Member)session.getAttribute("findid_member");
    	String id = find_member.getId();
    	log.info("find_member_ID = {}",id);
    	session.removeAttribute("findid_member");
    	return id;
    }
    
    @GetMapping("/test")
    public String test() {
    	return "success";
    }
    /*
     *-----------------------------------------------------------------------------------------------------
     *private 메소드
     *----------------------------------------------------------------------------------------------------- 	
     */
    
    //인증 번호 문자 보내는 코드
    private SingleMessageSentResponse Message(String phone, HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	String code = RandomStringUtils.randomNumeric(6);
		session.setAttribute("code", code);
		SingleMessageSentResponse reponse = loginService.sendMessage(phone,code);
    	return reponse;
    }
}