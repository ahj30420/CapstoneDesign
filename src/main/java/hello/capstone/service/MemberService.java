package hello.capstone.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hello.capstone.dto.Member;
import hello.capstone.dto.Shop;
import hello.capstone.exception.AlreadyBookmarkedShopException;
import hello.capstone.exception.FindPwException;
import hello.capstone.exception.InvalidPhoneNumberException;
//import hello.capstone.exception.FindPwException;
import hello.capstone.exception.LogInException;
import hello.capstone.exception.NicknameException;
import hello.capstone.exception.SignUpException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.MemberRepository;
import hello.capstone.repository.ShopRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final ShopRepository shopRepository;
	private final PasswordEncoder bCryptPasswordEncoder;
	
	/*
	 * 멤버의 멤버인덱스 조회
	 */
	public int getMemberIdx(Member member) {
		int idx = memberRepository.getMeberIdx(member);
		return idx;
	}
	
	/*
	 * 즐겨찾기 등록
	 */
	public void bookmarkRegistration(int memberIdx, int shopIdx) {
		
		List<Shop> MyBookmarkedShops = memberRepository.getMyBookmarkedShop(memberIdx);
		for (Shop bookmarkShop : MyBookmarkedShops) {
			int idx = shopRepository.getShopIdx(bookmarkShop);
			if (shopIdx == idx){
				throw new AlreadyBookmarkedShopException(ErrorCode.ALREADY_BOOKMARKED_SHOP,null);
			}
		}
		
		memberRepository.bookmarkRegistraion(memberIdx, shopIdx);
	}
	
	/*
	 * 즐겨찾기 취소
	 */
	@Transactional
	public void bookmarkDelete(int memberIdx, int shopIdx) {
		memberRepository.bookmarkDelete(memberIdx, shopIdx);
	}
	
	/*
	 * 즐겨찾기한 가게들의 인덱스 조회 
	 */
	public List<Shop> getMyBookmarkedShop(int memberIdx) {
		
		
		return memberRepository.getMyBookmarkedShop(memberIdx);
	}
	
	/*
	 * 닉네임 변경
	 */
	@Transactional
	
	public Member updateNickname(Member member, String nickname) {
		//닉네임이 15글자 이상은 수정x
		if( nickname.length() > 15) {
			throw new NicknameException(ErrorCode.NICKNAME_MORE_TAHN_15LETTERS, null);
		}
		memberRepository.updateNickname(member, nickname);
		member.setNickname(nickname);
		
		return member;
	}
	
	/*
	 * 회원 탈퇴
	 */
	@Transactional
	public void deleteMember(Member member) {
		memberRepository.deleteMember(member);
	}
	
	
	/*
	 * 회원정보 수정
	 */
	@Transactional
	public Member updateMember(Member oldMember, Member newMember) {
		
		if(newMember.getNickname().length() > 15) {
			throw new NicknameException(ErrorCode.NICKNAME_MORE_TAHN_15LETTERS, null);
		}
		
		//휴대폰 번호 유효성 검사
		if(newMember.getPhone().length() != 11 || newMember.getPhone().contains("-") ) {
			throw new InvalidPhoneNumberException(ErrorCode.INVALID_PHONE_NUMBER,null);
		}
		memberRepository.updateMember(oldMember, newMember);
		oldMember.setNickname(newMember.getNickname());
		oldMember.setName(newMember.getName());
		oldMember.setPhone(newMember.getPhone());
		
		return oldMember;
	}
	
	/*
	 * 비밀번호 찾기(아이디 유무 확인)
	 */
	public Member ID_verification(String id) {
		Member member = memberRepository.findById(id,"normal");
		
		if(member == null) {
			throw new FindPwException(ErrorCode.NONEXISTENT_MEMBER,null);
		}
		return member;
	}
	

	/*
	 * 아이디 찾기(이름, 휴대폰 번호 매칭)
	 */
	public Member Name_verification(String name, String phone) {
		Member member = memberRepository.findByName_Phone(name,phone,"normal");
		
		if(member == null) {
			throw new FindPwException(ErrorCode.NONEXISTENT_MEMBER,null);
		}
		return member;
	}
	/*
	 * 비밀번호 변경 (비밀번호 찾기에서)
	 */
	@Transactional
	public void updatepw(String id, String pw) {
		memberRepository.updatepw(id, pw, "normal");
	}
	
	
	/*
    * 비밀번호 일치 확인
    */
	   
	   public void pwCheck(Member member, String oldPw) {
	
			boolean pwCheck = bCryptPasswordEncoder.matches(oldPw, member.getPw());
			if(!pwCheck) {
		    	  throw new LogInException(ErrorCode.PASSWORD_MISMATCH, null);
		      }
	   }
	

	/*
	 * 비민번호 변경 (사용자가 의도적으로 비밀번호 변경을 원할 때)
	 */
	@Transactional
	public Member updatePwOnPurpose(Member member, String newPw) {
		
		member.setPw(newPw);
		memberRepository.updatepw(member.getId(), member.getPw(), "normal");
		
		
		return member;
	}
	
	//---------------------------------------------------------------------------------------------
	
	
}