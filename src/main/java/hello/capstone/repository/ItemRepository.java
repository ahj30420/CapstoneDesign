package hello.capstone.repository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

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
	
	
	/*
	 * 해당 가게의 모든 아이템 정보
	 */
	public List<Item> getItems(int shopidx){
		
		
		return itemMapper.getItems(shopidx); 
	}
	
	/*
    * 아이템 삭제
    */
	public void deleteItemEndtime(Timestamp now) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(now);
		itemMapper.deleteItemEndtime(time);
	}
	
	
}