package hello.capstone.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hello.capstone.dto.Alarm;
import hello.capstone.dto.Item;
import hello.capstone.dto.Member;
import hello.capstone.dto.Reservation;
import hello.capstone.dto.Shop;

@Mapper
public interface ItemMapper {

	void saveItem(Item item);
	
	void updateItem(Item item);
	
	Item findByItemIdx(int ItemIdx);
	
	Item findByShopIdxAndItemname(@Param("shopidx") int shopidx, @Param("itemname") String itemname);
	
	List<Item> getItems(@Param("shopidx") int shopidx);
	
	void itemDelete(Item item);
	
	int reservationCheck(Item item);
	
	void pushAlarm(@Param("shopidx") int shopidx);
	
	void deleteTimeoutAlarm();
	
	void deleteReadAlarm(@Param("shop") Shop shop, @Param("member") Member member);
	
	List<Map<String,Object>> getAlarm(@Param("memberidx") int memberidx);
	
	int getQuantity(int itemidx);
	
	void reservation(Reservation reservation);
	
	void reduceQuantity(Reservation reservation);
	
	void reservationConfirm(int ridx);
	
	void reservationDelete(int ridx);
	
	void reservationCancelBusiness(int reservationIdx);
	
	List<Map<String, Object>> getReservations(@Param("memberidx") int memberidx, @Param("confirm") String confirm);
	
	void increaseQuantity(@Param("itemidx")int itemidx, @Param("number")int number);
	
	void decreaseTrust(String time);
	
	void setConfirmToFalse(String time);
}