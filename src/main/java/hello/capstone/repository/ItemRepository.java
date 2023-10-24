package hello.capstone.repository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import hello.capstone.dto.Alarm;
import hello.capstone.dto.Item;
import hello.capstone.dto.Member;
import hello.capstone.dto.Reservation;
import hello.capstone.dto.Shop;
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
	public boolean saveitem(Item item, String method) {
		if(method.equals("register")) {
			log.info("repository_item = {}", item);
			itemMapper.saveitem(item);
		}
		else {
			itemMapper.modifyItem(item);
		}
		return true;
	}
	
	
	/*
	 * 상품 등록시 가게 고유 번호와 상품 이름으로 상품 중복 확인
	 */
	public Item findByShopIdx_itemname(int shopidx, String itemname) {
		return itemMapper.findByShopIdx_itemname(shopidx, itemname);
	}
	
	
	/*
	 * 해당 가게에서 마감되지 않은 모든 아이템 정보
	 */
	public List<Item> getItems(int shopidx){
		
		
		return itemMapper.getItems(shopidx); 
	}

	/*
	 * 아이템 삭제
	 */
	public void itemDelete(Item item) {
		 itemMapper.itemDelete(item);
	}
	
    /*
    * 예약자가 있는 상품인지 확인
    */
	public int reservationCheck(Item item) {
		return itemMapper.reservationCheck(item);
	}
	
	/*
	 * 알림 등록
	 */
	public void pushAlarm(int shopidx) {
		itemMapper.pushAlarm(shopidx);
	}
	
	
	/*
	 * 알림 가져오기
	 */
	public List<Map<String, Object>> getAlarm(int memberidx) {
		return itemMapper.getAlarm(memberidx);   
	}
	
	/*
	 * 읽은 알림 삭제
	 */
	public void deleteReadAlarm(Shop shop, Member member) {
		itemMapper.deleteReadAlarm(shop, member);
		
	}
	
	/*
	 * 24시간이 지난 알림 삭제
	 */
	public void deleteTimeoutAlarm() {
		itemMapper.deleteTimeoutAlarm();
		
	}
	
	/*
	 *  아이템 수량 조회
	 */
	public int getQuantityByitemIdx(int itemidx) {
		return itemMapper.getQuantity(itemidx);
	}
	
	/*
	 * 상품 예약
	 */
	public void reservation(Reservation reservation) {
		itemMapper.reservation(reservation);
		itemMapper.reduceQuantity(reservation);
	}
	
	/*
	 * 상품 예약 확인(상업자가 확인 버튼 클릭)
	 */
	public void reservationConfirm(int ridx) {
		itemMapper.reservationConfirm(ridx);
	}
	
	/*
	 * 상품 예약 취소
	 */
	public void reservationCancel(int ridx, int itemidx, int number) {
		itemMapper.reservationDelete(ridx);
		itemMapper.increaseQuantity(itemidx, number);
	}
	
    /*
     * 예약 상품 리스트 조회
     */
    public List<Map<String, Object>> getReservations(int memberidx){
    	return itemMapper.getReservations(memberidx);
    }
	
	/*
	 * 예약자 실제 상품 결제 확인하여 신뢰점수 차감
	 */
	public void checkTrust(Timestamp now) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(now);
		itemMapper.decreaseTrust(time);
		itemMapper.setConfirmToFalse(time);
	}
}