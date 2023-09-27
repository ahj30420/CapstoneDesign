package hello.capstone.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.capstone.dto.Member;
import hello.capstone.service.LoginService;
import hello.capstone.service.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@ResponseBody
@AllArgsConstructor
public class OAuthController {

	private final OAuthService oauthService;
	private final LoginService loginService;
	/*
	 * 카카오 로그인 인가토큰 받고, 회원 정보 받아오기
	 */
    @ResponseBody
    @GetMapping("/kakao/oauth")
    public HashMap<String, Object> getKakaoTokenAndInfo(@RequestParam String code) {
        log.info("code={}",code);
        
        String accessToken = oauthService.getKakaoAccessToken(code);
        HashMap<String, Object> infos = oauthService.getUserInfo(accessToken);
        
        for(String infoKey : infos.keySet()){	
        	log.info("info = {}, {}",infoKey, infos.get(infoKey));
        	
        }
        log.info("acttn = {}", accessToken);  
        
        HashMap<String, Object> token = new HashMap<>();
        token.put("url", "/kakao/oauth/signUp");
        token.put("infos", infos);
             
        
        return token;
    }
    
    
    /*
     * 카카오 - 받은 정보로 비회원이라면 회원가입
     */
    @PostMapping("/kakao/oauth/signUp")
    public String kakaoSignUp(@RequestBody Member member , HttpServletRequest request) {
    	log.info("name = {}", member.getName());
    	log.info("id = {}", member.getId());
    	
    	member = loginService.kakaoSignUp(member);
    	
    	
    	HttpSession session = request.getSession();
    	session.setAttribute("member", member);
    		
    	return "/home_user";
    	
    }
    
    /*
     * 네이버 토큰받고 로그인시도 - DB에 정보가 없는 회원이라면 회원가입 후 로그인 처리
     */
    
    @PostMapping("/login/oauth2/Naver_loading2")
    public String naverOAuthRedirect(@RequestBody Map<String, String> codeMap, HttpServletRequest request) {
        String code = (String)codeMap.get("code");
        String state =  (String)codeMap.get("state");
        log.info("code = {}", code);
        log.info("state = {}", state);
        
    	ResponseEntity<String> accessTokenResponse = oauthService.getNaverAccessToken(code, state);
    	log.info("accessToken={}", accessTokenResponse.getBody());
    	HashMap<String, Object> naverInfo = oauthService.getNaverInfo(accessTokenResponse);
    	
    	Member naverMember = new Member();
    	naverMember.setId((String)naverInfo.get("id"));
    	naverMember.setName((String)naverInfo.get("name"));
    	naverMember.setPhone((String)naverInfo.get("phone"));
    	
    	naverMember = loginService.naverSignUp(naverMember);
    	    	
    	HttpSession session = request.getSession();
    	session.setAttribute("member", naverMember);
    	log.info("Naver_member = {}",naverMember);
    	return "/home_user";   
    }
    
    
    
}