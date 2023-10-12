package hello.capstone.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hello.capstone.dto.Member;
import hello.capstone.dto.Shop;

@Mapper
public interface MemberMapper {

	void bookmarkRegistraion(@Param("memberidx") int memberidx, @Param("shopidx") int shopidx );
	
	void bookmarkDelete(@Param("memberidx") int memberidx, @Param("shopidx") int shopidx);
	
	List<Shop> getMyBookmarkedShop(@Param("memberidx") int memberidx);
	
	void updateNickname(@Param("member") Member member, @Param("nickname") String nick);
	
	void deleteMember(Member member);
	
	void updateMember(@Param("oldMember") Member oldMember, @Param("newMember") Member newMember);
	
	
}