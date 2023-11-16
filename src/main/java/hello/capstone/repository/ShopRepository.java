package hello.capstone.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import hello.capstone.dto.Shop;
import lombok.RequiredArgsConstructor;

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
	public void saveShop(Shop shop) {
		shopMapper.saveShop(shop);
	}
	
	
	/*
	 * 매장 수정
	 */
	public void updateShop(Shop shop) {
		shopMapper.modifyShop(shop);
	}
	
	/*
	 * 매장 삭제
	 */
	public void deleteShop(int shopIdx) {
		shopMapper.deleteShop(shopIdx);
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
	
	public Shop getShopByItemIdx(int itemidx) {
		return shopMapper.getShopByItemIdx(itemidx);
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
	public List<Shop> getShopByMember(int memberIdx){
		return shopMapper.getShopByMember(memberIdx);
	}
	/*
	 * 가격 필터에 해당되는 가게 조회
	 */
	public List<Shop> runPriceFilter(int maxPrice, int minPrice){
		return shopMapper.runPriceFilter(maxPrice, minPrice);
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
	public void setRatings(int shopIdx, double rating) {
		shopMapper.setRatings(shopIdx, rating);
	}
	
	   /*
     * 해당 아이템 별로 예약자 리스트 조회
     */
    public List<Map<String, Object>> getItemReservations(int itemIdx){
       return shopMapper.getItemReservations(itemIdx);
    }  
}