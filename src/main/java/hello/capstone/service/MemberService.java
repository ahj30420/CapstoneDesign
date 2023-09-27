package hello.capstone.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.capstone.dto.Member;
import hello.capstone.dto.Shop;
import hello.capstone.exception.AlreadyBookmarkedShopException;
import hello.capstone.exception.FindPwException;
import hello.capstone.exception.NicknameException;
import hello.capstone.exception.SignUpException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.MemberRepository;
import hello.capstone.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final ShopRepository shopRepository;
	
	public int getMeberIdx(Member member) {
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
		
		if(oldMember.getNickname().equals(newMember.getNickname()) || 
				newMember.getNickname().length() > 15) {
			throw new NicknameException(ErrorCode.NICKNAME_DUPLICATED_OR_MORE_TAHN_15LETTERS, null);
		}
		
		memberRepository.updateMember(oldMember, newMember);
		oldMember.setNickname(newMember.getNickname());
		oldMember.setPw(newMember.getPw());
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
	
	public void updatepw(String id, String pw) {
		memberRepository.updatepw(id, pw, "normal");
	}
	
}