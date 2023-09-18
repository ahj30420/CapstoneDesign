package hello.capstone.repository;

import org.apache.ibatis.annotations.Mapper;

import hello.capstone.dto.Member;

@Mapper
public interface LoginMapper {

   void save(Member member);
   
   Member findbyid(String id); //마지막수정 09/15 16시 41분
}