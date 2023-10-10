package hello.capstone.repository;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hello.capstone.dto.Ratings;

@Mapper
public interface RatingsMapper {

	Ratings existingRatings(@Param("shopidx") int shopidx, @Param("memberidx") int memberidx);
	
	void updateRatings(Ratings rating);
	
	void setRatings(Ratings rating);
	
	HashMap<String, Integer> getSumCount(int shopidx);
}
