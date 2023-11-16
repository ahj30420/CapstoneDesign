package hello.capstone.repository;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import hello.capstone.dto.Ratings;
import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class RatingsRepository {

	private final RatingsMapper ratingsMapper;
	
	/*
	 * 해당 맴버가 해당 가게에 별점을 주었었는지 확인
	 */
	public boolean existingRatings(int shopidx, int memberidx) {
		Ratings rating = ratingsMapper.existingRatings(shopidx, memberidx);
		
		if(rating == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/*
	 * 해당 가게에 대한 사용자의 별점 수정
	 */
	public void updateRatings(Ratings rating) {
		ratingsMapper.updateRatings(rating);
	}
	
	/*
	 * 해당 가게에 대한 사용자 별점 추가
	 */
	public void setRatings(Ratings rating) {
		ratingsMapper.setRatings(rating);
	}
	
	/*
	 * 해당 가게에 몇명의 사람들이 평점을 달았고 평점의 합이 얼마인지 조회 
	 */
	public HashMap<String,Integer> getSumCount(int shopidx){
		return ratingsMapper.getSumCount(shopidx);
	}
}