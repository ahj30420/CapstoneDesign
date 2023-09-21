package hello.capstone.repository;


import org.springframework.stereotype.Repository;

import hello.capstone.dto.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

   public final LoginMapper loginMapper;
   /*
    * 회원정보저장 마지막수정 09/18 23시 20분
    */
   public boolean save(Member member) {
      
      loginMapper.save(member);
      return true;
   }
   
   /*
    * 카카오회원정보저장
    */
   public Member saveSocial(Member member) {
      
      loginMapper.saveSocial(member);
      Member findMember = findById(member.getId(), member.getSocial());
      return findMember;
   }
   
   
   /*
    * 아이디로 회원 검색 
    * */
   public Member findById(String id, String social) {
	   Member findMember = loginMapper.findbyid(id, social);
	   
	   return findMember;
   }
   
 
   /*
    * 회원 인덱스
    */
   public int getMeberIdx(Member member) {
	   int idx = loginMapper.getMeberIdx(member);
  
	   return idx;
   }
  
   
   
   
   
}