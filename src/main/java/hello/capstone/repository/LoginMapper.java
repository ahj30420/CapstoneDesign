package hello.capstone.repository;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hello.capstone.dto.Member;

@Mapper
public interface LoginMapper {

   void save(Member member);
   
   void saveSocial(Member member);
   
   Member findbyid(@Param("id") String id, @Param("social") String social);  
    
   int getMeberIdx(Member member);
}