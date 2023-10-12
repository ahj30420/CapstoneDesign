package hello.capstone.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.capstone.dto.Member;
import hello.capstone.dto.Shop;
import hello.capstone.dto.Alarm;
import hello.capstone.exception.LogInException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.service.ItemService;
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
	private final ItemService itemService;
	
	
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
	 * 즐겨찾기 취소
	 */
	@DeleteMapping("/bookmark/delete")
	public String bookmarkDelete(HttpSession session, @RequestBody Shop shop) {
		Member member = (Member) session.getAttribute("member");
		
		log.info("member = {} ", member);
		
		int memberIdx = memberService.getMeberIdx(member);
		int shopIdx = shopService.getShopIdx(shop);
		
		memberService.bookmarkDelete(memberIdx, shopIdx);
		

		return "/home_user";
	}
	
	
	/*
	 * 즐겨찾기 목록 조회
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
		
		String nickname = nick.get("nickname");
		Member member = (Member) session.getAttribute("member");
		
		memberService.updateNickname(member, nickname);
		session.setAttribute("member", member);
		log.info("member = {}", member);
		
		return "home_user";
	}
	
	/*
	 * 비밀번호 수정
	 */
	@PutMapping("/update/pw")
	public String updatePw(@RequestBody HashMap<String,String> pwMap, HttpSession session) {
		log.info("pwMap = {}", pwMap);
		String oldPw = pwMap.get("oldpw");
		String newPw = pwMap.get("newpw");
		Member member = (Member)session.getAttribute("member");
		memberService.pwCheck(member, oldPw);
		
		Member newMember = memberService.updatePwOnPurpose(member, newPw);
		session.setAttribute("member", newMember);
		
		return "/";
	}
	
	/*
	 * 회원정보 수정
	 */
	@PutMapping("/update/info")
	public String updateInfo(@RequestBody HashMap<String, String> newMemberMap, HttpSession session) {
		log.info("newMemberMap = {}", newMemberMap);
		Member oldMember = (Member) session.getAttribute("member");
		String newName = newMemberMap.get("newname");
		String newNickname = newMemberMap.get("newnickname");
		String newPhone = newMemberMap.get("newphone");
		
		Member newMember = new Member();
		newMember.setName(newName);
		newMember.setNickname(newNickname);
		newMember.setPhone(newPhone);
		
		newMember = memberService.updateMember(oldMember, newMember);
		
		session.setAttribute("member", newMember);
		
		return "home_user";
	}
	
	
	/*
	 * 회원 탈퇴
	 */
	@DeleteMapping("/delete")
	public String deleteMember(HttpSession session, String pw) {
		
		Member member = (Member) session.getAttribute("member");
		memberService.pwCheck(member, pw);
		memberService.deleteMember(member);
		
		session.removeAttribute("member");
		
		return "login";
	}
	
	/*
	 * 알람 가져오기
	 */
	@GetMapping("/getAlarm")
	public List<Shop> getAlarm(HttpSession session){
		Member member = (Member) session.getAttribute("member");
		List<Shop> alarmShop = new ArrayList<Shop>();
		
		List<Alarm> shopIdxes = itemService.getAlarm(memberService.getMeberIdx(member));
		for (Alarm idx : shopIdxes) {
			alarmShop.add(shopService.getShopByIdx(idx.getShopIdx()));
		}
		
		return alarmShop;
	}
	
	/*
	 * 읽은 알람 삭제
	 */
	@DeleteMapping("deleteReadAlarm")
	public void deleteReadAlarm(@RequestBody Shop shop, HttpSession session) {
		itemService.deleteReadAlarm(shop, (Member)session.getAttribute("member"));
	}
	
}