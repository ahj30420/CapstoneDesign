package hello.capstone.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hello.capstone.dto.Alarm;
import hello.capstone.dto.Item;
import hello.capstone.dto.Member;
import hello.capstone.dto.Reservation;
import hello.capstone.dto.Shop;

@Mapper
public interface ItemMapper {

	void saveitem(Item item);
	
	void modifyItem(Item item);
	
	Item findByShopIdx_itemname(@Param("shopidx") int shopidx, @Param("itemname") String itemname);
	
	List<Item> getItems(@Param("shopidx") int shopidx);
	
	void deleteItemEndtime(@Param("time") String time);
	
	void pushAlarm(@Param("shopidx") int shopidx);
	
	void deleteTimeoutAlarm();
	
	void deleteReadAlarm(@Param("shop") Shop shop, @Param("member") Member member);
	
	List<Alarm> getAlarm(@Param("memberidx") int memberidx);
	
	int getQuantity(int itemidx);
	
	void reservation(Reservation reservation);
	
	void reduceQuantity(Reservation reservation);
	
	void reservationConfirm(int ridx);
	
	void reservationDelete(int ridx);
	
	void increaseQuantity(int itemidx, int number);
	
	void checkTrust(String time);
}