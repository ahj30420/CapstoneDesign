package hello.capstone.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}