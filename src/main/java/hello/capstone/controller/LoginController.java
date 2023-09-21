package hello.capstone.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.capstone.dto.Member;
import hello.capstone.service.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
	
	private final LoginService loginService;
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
    
}