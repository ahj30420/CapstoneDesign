package hello.capstone.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.capstone.dto.Member;
import hello.capstone.dto.Shop;
import hello.capstone.exception.LogInException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.service.MemberService;
import hello.capstone.service.ShopService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	private final ShopService shopService;
	
	
	/*
	 * 즐겨찾기 등록
	 */
	@PostMapping("/bookmark/registration")
	public String bookmarkRegistration(HttpSession session, @RequestBody Shop shop) {
		Member member = (Member) session.getAttribute("member");
		
		log.info("member = {} ", member);
		
		int memberIdx = memberService.getMeberIdx(member);
		int shopIdx = shopService.getShopIdx(shop);
		
		memberService.bookmarkRegistration(memberIdx, shopIdx);
		

		return "/home_user";
	}
	
	/*
	 * 즐겨찾기 조회
	 */
	@GetMapping("/bookmark/check")
	public List<Shop> bookmarkCheck(HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		
		int memberIdx = memberService.getMeberIdx(member);
		
		List<Shop> MyBookmarkedShops = memberService.getMyBookmarkedShop(memberIdx);
		log.info("MyBookmarkedShops = {} ", MyBookmarkedShops);
		
		
		
		return MyBookmarkedShops;
	}
	
	/*
	 * 닉네임 변경
	 */
	@PutMapping("/update/nickname")
	public String updateNickname(@RequestBody HashMap<String,String> nick, HttpSession session) {
		log.info("닉네임 ={} ", nick.get("nickname"));
		String nickname = nick.get("nickname");
		Member member = (Member) session.getAttribute("member");
		
		memberService.updateNickname(member, nickname);
		session.setAttribute("member", member);
		log.info("member = {}", member);
		
		return "home_user";
	}
	
	/*
	 * 회원정보 수정
	 */
	@PutMapping("/update/info")
	public String updateInfo(@RequestBody Member newMember, HttpSession session) {
		Member oldMember = (Member) session.getAttribute("member");
		newMember = memberService.updateMember(oldMember, newMember);
		
		session.setAttribute("member", newMember);
		
		return "home_user";
	}
	
	
	/*
	 * 회원 탈퇴
	 */
	@DeleteMapping("/delete")
	public String deleteMember(HttpSession session) {

		Member member = (Member) session.getAttribute("member");
		memberService.deleteMember(member);
		
		session.removeAttribute("member");
		
		return "login";
	}
	
	/*
	 * 비밀번호 일치 확인
	 */
	@GetMapping("/info/pwcheck")
	public void passwordCheck(HttpSession session, @RequestParam("pw") String pw) {
		
		String realPw = ((Member)session.getAttribute("member")).getPw();
		if(!(realPw.equals(pw))) {
	    	  throw new LogInException(ErrorCode.PASSWORD_MISMATCH, null);
	      }
	
	}
}