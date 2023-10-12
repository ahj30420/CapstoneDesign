package hello.capstone.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import hello.capstone.dto.Member;
import hello.capstone.dto.Shop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Repository
@RequiredArgsConstructor
public class ShopRepository {
	
	private final ShopMapper shopMapper;
	
	
	/*
	 * 주소로 매장 검색
	 */
	public Shop findByAddress(String address) {
		Shop findShop = shopMapper.findByAddress(address);
		
		return findShop;
	}
	
	/*
	 * 매장 등록
	 */
	public boolean saveShop(Shop shop, String method) {
		if(method.equals("register")) {
			shopMapper.saveShop(shop);
		}
		else {
			shopMapper.modifyShop(shop);
		}
		log.info("repository shop = {}", shop);
		return true;
	}
	
	/*
	 * shop 인덱스조회
	 */
	public int getShopIdx(Shop shop) {
	   int idx = shopMapper.getShopIdx(shop);
  
	   return idx;
	}
	
	public Shop getShopByIdx(int shopidx) {
		return shopMapper.getShopByIdx(shopidx);
		
	}
	
	/*
	 * shop Mark표시 테스트 (모든 가게)
	 */
	public List<Shop> getShops(){
		return shopMapper.getShops();
	}
	
	
	/*
	 * 상업자 본인이 올린 가게
	 */
	public List<Shop> getShopByMember(int memberidx){
		return shopMapper.getShopByMember(memberidx);
	}
	/*
	 * 가격 필터에 해당되는 가게 조회
	 */
	public List<Shop> runPriceFilter(int price){
		return shopMapper.runPriceFilter(price);
	}
	
	/*
	 * 별점 필터에 해당되는 가게 조회
	 */
	public List<Shop> runRatingFilter(double rating){
		return shopMapper.runRatingFilter(rating);
	}
	
	/*
	 * 가게 별점 설정하기
	 */
	public void setRatings(int shopidx, double rating) {
		shopMapper.setRatings(shopidx, rating);
	}
}