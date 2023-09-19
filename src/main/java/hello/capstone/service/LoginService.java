package hello.capstone.service;



import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import hello.capstone.dto.Member;
import hello.capstone.exception.LogInException;
import hello.capstone.exception.SignUpException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {
   
   private final MemberRepository memberRepository;
   /*
    * 회원가입 - 마지막 수정 09/20/ 23시 20분
    * */
   public boolean signUp(Member member) {
      
      
      //.ifPresent()는 memberRepository.findById 실행 시 오류 던져주기 위함
      Optional.ofNullable(memberRepository.findById(member.getId(),"normal"))
         .ifPresent(user->{
            throw new SignUpException(ErrorCode.DUPLICATED_USER_ID,null);
         });
      
      long miliseconds = System.currentTimeMillis();
      Date redate = new Date(miliseconds);
      member.setNickname(createRandomNickname());
      member.setSocial("normal");
      member.setRedate(redate);
      
      return memberRepository.save(member);
       
   }
   

   /*
    * 카카오 회원가입 - 
    * */
   public boolean kakaoSignUp(Member member) {
      
      
      Member findMember = memberRepository.findById(member.getId(),"kakao");
      
      
      if(Objects.isNull(findMember)) {
         long miliseconds = System.currentTimeMillis();
         Date redate = new Date(miliseconds);
         
         String uuidPw = (UUID.randomUUID()).toString();
         
         member.setPw(uuidPw);
         member.setNickname(createRandomNickname());
         member.setSocial("kakao");
         member.setRole("사용자");
         member.setRedate(redate);
         
         return memberRepository.saveSocial(member);
      }
      return true;
       
   }
   /*
    * 카카오 회원가입 - 
    * */
   public boolean naverSignUp(Member member) {
      
      
      Member findMember = memberRepository.findById(member.getId(),"naver");
      
      
      if(Objects.isNull(findMember)) {
         long miliseconds = System.currentTimeMillis();
         Date redate = new Date(miliseconds);
         
         String uuidPw = (UUID.randomUUID()).toString();
         
         member.setPw(uuidPw);
         member.setNickname(createRandomNickname());
         member.setSocial("naver");
         member.setRole("사용자");
         member.setRedate(redate);
         
         
         log.info("member ={}", member);
         return memberRepository.saveSocial(member);
      }
      return true;
       
   }
   
   
   /*
    * 로그인 
    */
   public Member login(String id, String pw) {
      
      
      Member userMember = memberRepository.findById(id,"normal");
      boolean pwCheck = passwordCheck(userMember, pw);
      //여기도 Exception 처리하는게 좋을듯
      
      if(pwCheck == false) {
         log.info("fail");
         return null;
      }
      //false 에러를 어떻게 처리할지 모르갰음
      return userMember;
   }
   
   
   
   
   
/*
 *-----------------------------------------------------------------------------------------------------
 *private 메소드
 *-----------------------------------------------------------------------------------------------------    
 */
   
   //비밀번호 일치 확인
   private boolean passwordCheck(Member userMember, String pw) {
      boolean pwCheck = true;
      if(!(userMember.getPw().equals(pw))) {
            throw new LogInException(ErrorCode.PASSWORD_MISMATCH, null);
         }
      
      return pwCheck;
   }
   
   //중복회원 검사
   private Member duplicateCheck(Member member) {
      Member findMember = memberRepository.findById(member.getId(),"normal");
      
      return findMember;
   }
   //닉네임 임의 random String
   private String createRandomNickname() {
      int randomNicknameLen = 8;
      boolean useLetters = true;
      boolean useNumbers = false;
      String randomNick = RandomStringUtils.random(randomNicknameLen, useLetters, useNumbers);
         
      log.info("create random nickname = {} ", randomNick);
         
      return randomNick;
   }
}









