package hello.capstone.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import hello.capstone.service.OAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class OAuthController {

	private final OAuthService oauthService;
	
    @ResponseBody
    @GetMapping("/login/oauth2/loading")
    public void kakaoCallback(@RequestParam String code) {
        log.info("code={}",code);
        
        String accessToken = oauthService.getKakaoAccessToken(code);
        HashMap<String, Object> infos = oauthService.getUserInfo(accessToken);
        
        for(String infoKey : infos.keySet()){	
        	log.info("info = {}, {}",infoKey, infos.get(infoKey));
        	
        }
        log.info("acttn = {}", accessToken);
        
    }
    
    @GetMapping("/login/oauth2/Naver_loading2")
    public String naverOAuthRedirect(@RequestParam String code, @RequestParam String state) {
       
    	ResponseEntity<String> accessTokenResponse = oauthService.getNaverAccessToken(code, state);
    	log.info("accessToken={}", accessTokenResponse.getBody());
    	String naverinfo = oauthService.getNaverInfo(accessTokenResponse);
    	
    	return naverinfo;   
    }
}
  