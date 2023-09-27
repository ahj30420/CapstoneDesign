package hello.capstone.repository;

import org.springframework.stereotype.Repository;

import hello.capstone.dto.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ItemRepository {
	
	private final ItemMapper itemMapper;

	/*
	 * 상품 등록
	 */
	public boolean saveitem(Item item) {
		itemMapper.saveitem(item);
		return true;
	}
	
	/*
	 * 상품 등록시 가게 고유 번호와 상품 이름으로 상품 중복 확인
	 */
	public Item findByShopIdx_itemname(int shopidx, String itemname) {
		return itemMapper.findByShopIdx_itemname(shopidx, itemname);
	}
	
}
