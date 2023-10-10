package hello.capstone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import hello.capstone.dto.Item;
import hello.capstone.exception.SaveItemException;
import hello.capstone.exception.SaveShopException;
import hello.capstone.exception.TimeSettingException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemService {

	private final ItemRepository itemRepository;
	
	/*
	 * 아이템 등록
	 */
	public boolean itemsave(Item item) {
		Optional.ofNullable(itemRepository.findByShopIdx_itemname(item.getShopidx(), item.getItemname()))
        .ifPresent(user->{
           throw new SaveItemException(ErrorCode.DUPLICATED_ITEM,null);
        });
     
     
     
	    //MySql의 Timestamp는 타임존을 반영하기 때문에 9시간 전으로 저장이 됨. 그걸 맞추기위해 9시간을 더해줌
	    Timestamp startTimeForSeoul = new Timestamp(item.getStarttime().getTime() + (9 * 60 * 60 * 1000));
	    Timestamp endTimeForSeoul = new Timestamp(item.getEndtime().getTime() + (9 * 60 * 60 * 1000));
	    item.setStarttime(startTimeForSeoul);
	    item.setEndtime(endTimeForSeoul);
	     
	    int timeOut = startTimeForSeoul.compareTo(endTimeForSeoul);
	    if(timeOut >= 0) {
	       throw new TimeSettingException(ErrorCode.TIME_SETTING_ERROR,null);
	    }
	     
	    itemRepository.saveitem(item);
	     
	    return true;
  }
	
	/*
	 * 아이템 가져오기
	 */
	
	public List<Item> getItems(int shopIdx){
		List<Item> items = new ArrayList<Item>();
		items = itemRepository.getItems(shopIdx);
		
		return items;
		
	}
	
   /*
    * 1분마다 실행되는 cron표현식 item들에 endtime을 확인하여 시간이 지나면 자동 삭제
    */
   @Scheduled(cron ="0 * * * * *")
   public void deleteItemEndtime() {
      LocalDateTime now = LocalDateTime.now();
      Timestamp timestamp = Timestamp.valueOf(now);
      
        // 현재 시간보다 이전인 튜플 삭제
      itemRepository.deleteItemEndtime(timestamp);
   }
}