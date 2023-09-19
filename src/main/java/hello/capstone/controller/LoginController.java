package hello.capstone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hello.capstone.dto.Member; 
import hello.capstone.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
	 
	private final LoginService loginService;
	/*
	 * 마지막수정 09/18 23시 20분 
	 * @RequestBody
	 * sign_up 반환값 boolean로 변경
	 * */
    @PostMapping("/join")
    public boolean sign_up(@RequestBody Member member ){
    	
    	log.info("id ={}",member.getId());
    	log.info("pw={}",member.getPw());
    	log.info("name ={}",member.getName());
    	log.info("phone={}",member.getPhone());
    	log.info("role={}",member.getRole());
    	
    	boolean success = loginService.signUp(member);
    	return success;
    }  
	/* 테스트용
	 * @GetMapping("/join2") public boolean sign_up2(@ModelAttribute Member member){
	 * 
	 * log.info("id ={}",member.getId()); log.info("pw={}",member.getPw());
	 * log.info("name ={}",member.getName());
	 * log.info("phone={}",member.getPhone()); log.info("role={}",member.getRole());
	 * 
	 * boolean success = loginService.signUp(member); return success; }
	 */
    
    @PostMapping("/login_attempt")
    public String login(@RequestBody HashMap<String, Object> loginMap, HttpServletRequest request) {
       String id = (String) loginMap.get("id");
       String pw = (String) loginMap.get("pw");
      
       if(id.isEmpty()) {
          return "redirect:/login";
       }
       Member userMember = loginService.login(id, pw);
       HttpSession session = request.getSession();
       
       session.setAttribute("id", userMember.getId());
       session.setAttribute("name", userMember.getName());
       session.setAttribute("nickname", userMember.getNickname());
       
       log.info("loginId={}",userMember.getId());
       log.info("loginName={}",userMember.getName());
       
       return "/home";
    }
	/*테스트용
	 * @GetMapping("/login_attempt2") public String login(@RequestParam String
	 * id, @RequestParam String pw, HttpServletRequest request) {
	 * 
	 * if(id.isEmpty()) { return "redirect:/login"; } Member userMember =
	 * loginService.login(id, pw); HttpSession session = request.getSession();
	 * 
	 * session.setAttribute("id", userMember.getId()); session.setAttribute("name",
	 * userMember.getName()); session.setAttribute("nickname",
	 * userMember.getNickname());
	 * 
	 * log.info("loginId={}",userMember.getId());
	 * log.info("loginName={}",userMember.getName());
	 * 
	 * return "/home"; }
	 */
}