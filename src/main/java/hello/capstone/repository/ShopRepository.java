package hello.capstone.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

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
	public boolean saveShop(Shop shop) {
		log.info("reposotiry shop = {}", shop);
		shopMapper.saveShop(shop);
		return true;
	}
	
	/*
	 * shop 인덱스조회
	 */
	public int getShopIdx(Shop shop) {
		   int idx = shopMapper.getShopIdx(shop);
	  
		   return idx;
	}
	
	/*
	 * shop Mark표시 테스트
	 */
	public List<Shop> getShops(){
		return shopMapper.getShops();
	}
}