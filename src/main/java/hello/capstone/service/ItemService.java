package hello.capstone.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import hello.capstone.dto.Coordinates;
import hello.capstone.dto.Item;
import hello.capstone.dto.Member;
import hello.capstone.dto.Reservation;
import hello.capstone.dto.Shop;
import hello.capstone.exception.ExistReservationException;
import hello.capstone.exception.NullPhoneException;
import hello.capstone.exception.QuantityException;
import hello.capstone.exception.SaveItemException;
import hello.capstone.exception.TimeSettingException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.ItemRepository;
import hello.capstone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;


@Slf4j
@RequiredArgsConstructor
@Service
public class ItemService {

	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	
   @Value("${itemfile.dir}")
   private String fileDir;
	
	/*
	 * 아이템 저장
	 */
	public void saveItem(Item item) 
			throws IllegalStateException, IOException{
		
		//이미지 파일 이름 저장
		if(item.getImageFile() != null) {
	         String fullPath = fileDir + item.getImageFile().getOriginalFilename();
	         item.getImageFile().transferTo(new File(fullPath));
	         item.setImage(item.getImageFile().getOriginalFilename());
	      }
		
		//중복아이템 검사
		Optional.ofNullable(itemRepository.findByShopIdxAndItemname(item.getShopidx(), item.getItemname()))
		.ifPresent(user->{
			throw new SaveItemException(ErrorCode.DUPLICATED_ITEM,null);
		});
		
		//시작시간보다 마감시간이 빠른지 검사
		int timeOut = item.getStarttime().compareTo(item.getEndtime());
		if(timeOut >= 0) {
			throw new TimeSettingException(ErrorCode.TIME_SETTING_ERROR,null);
		}
		
		itemRepository.saveItem(item);
		
		itemRepository.pushAlarm(item.getShopidx());
		
	}
	
	/*
	 * 아이템 수정
	 */
	public void updateItem(Item item, MultipartFile imageFile) 
			throws IllegalStateException, IOException {
		
		//이미지 파일이 새로 바뀐 경우
		if(imageFile != null) {
			String fullPath = fileDir + imageFile.getOriginalFilename();
			log.info("파일 저장 fullPath ={}",fullPath);
			imageFile.transferTo(new File(fullPath));
			item.setImage(imageFile.getOriginalFilename());
		}
		log.info("Item = {}", item);
		
		//시작시간보다 마감시간이 빠른지 검사
		int timeOut = item.getStarttime().compareTo(item.getEndtime());
		if(timeOut >= 0) {
			throw new TimeSettingException(ErrorCode.TIME_SETTING_ERROR,null);
		}
		
		itemRepository.updateItem(item);
	}
	
	/*
	 * 아이템 가져오기
	 */
	
	public List<Item> getItems(int shopIdx){
		List<Item> items = new ArrayList<Item>();
		items = itemRepository.getItems(shopIdx);
		
		for(int i = 0; i < items.size(); i++) {
			Timestamp starttime = new Timestamp(items.get(i).getStarttime().getTime() - (9 * 60 * 60 * 1000));
			Timestamp endtime = new Timestamp(items.get(i).getEndtime().getTime() - (9 * 60 * 60 * 1000));
			
			items.get(i).setStarttime(starttime);
			items.get(i).setEndtime(endtime);
		}
		
		return items;
		
	}
	
	/*
	 * 아이템 삭제
	 */
	public void itemDelete(Item item) {
		// 아이템을 예약한 사람이 있는지 확인
		if(itemRepository.reservationCheck(item) != 0){
			throw new ExistReservationException(ErrorCode.EXIST_RESERVATION_PERSON,null);
		}
		itemRepository.itemDelete(item);
		
		
	}
	/*
	 * 인덱스로 아이템찾기
	 */
	public Item findByItemIdx(int itemIdx) {
		return itemRepository.findByItemIdx(itemIdx);
	}
	
	/*
	 * 알림 가져오기
	 */
	public List<Map<String, Object>> getAlarm(int memberidx){
		List<Map<String, Object>> alarms = itemRepository.getAlarm(memberidx);
		
		for (Map<String, Object> alarm : alarms) {
			int howBefore = getHowBefore((Timestamp)alarm.get("before"));	
			alarm.replace("before", howBefore);
		}
		
		return alarms;
	}
	
	/*
	 * 읽은 알람 삭제
	 */
	public void deleteReadAlarm(Shop shop, Member member) {
		itemRepository.deleteReadAlarm(shop, member);
	}
	
	
	
	/*
	 * 1분마다 실행되는 cron표현식 item들에 마감시간을 확인하여 신뢰점수 차감
	 */
	@Scheduled(cron ="0 * * * * *")
	public void checkItemEndtime() {
		log.info("item @Scheduled 실행");
		LocalDateTime now = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(now);

        // 현재 시간보다 이전인 아이템 삭제
		itemRepository.checkTrust(timestamp);
	}
	
	/*
	 * 1분마다 실행되는 cron표현식 24시간만 유지되는 알림쪽지 
	 */
	@Scheduled(cron ="0 * * * * *")
	public void deleteTimeoutAlarm() {
		log.info("alarm @Scheduled 실행");
		itemRepository.deleteTimeoutAlarm();
	}
	
	/*
	 *  상품 예약
	 */
	public void reservation(Reservation reservation, String shopname, String itemname, String name, String phone) {
		
		int number = reservation.getNumber();
		int itemidx = reservation.getItemidx();
		int quantity = itemRepository.getQuantityByitemIdx(itemidx);
		
		if(phone == null) {
			throw new NullPhoneException(ErrorCode.NULL_PHONE, null);
		}
		
		if(quantity < number) {
			throw new QuantityException(ErrorCode.EXCESS_QUANTITY,null);
		}
		
		long miliseconds = System.currentTimeMillis();
		Date redate = new Date(miliseconds);
		reservation.setRedate(redate);
		
		String content = "[재고30]\n" + name + "님 정상적으로 예약이 완료되었습니다.\n\n" + "가게 이름: " + shopname + "\n상품명: " + itemname + "\n수량: " + number;
		
		itemRepository.reservation(reservation);
		sendMessage(phone, content);
		
	}
	
	/*
	 *  상품 예약 확인(상업자가 확인 버튼 클릭)
	 */
	public void reservationConfirm(int ridx) {
		itemRepository.reservationConfirm(ridx);
	}
	
	/*
	 * 상품 예약 취소
	 */
	public void reservationCancel(List<Map<String, Object>> reservationinfo, String name, String phone) {
		
		for(Map<String, Object> info : reservationinfo) {
			int ridx = (int)info.get("reservationidx");
			int itemidx = (int)info.get("itemidx");
			int number = (int)info.get("number");
			itemRepository.reservationCancel(ridx, itemidx, number);
		}
		String content = "[재고30]\n" + name + "님 정상적으로 예약이 취소되었습니다.\n";
		sendMessage(phone, content);
	}
	
    /*
     * 예약 상품 리스트 조회
     */
    public List<Map<String, Object>> getReservations(int memberidx , String confirm){
    	return itemRepository.getReservations(memberidx, confirm);
    }
    
    /*
     * 상품 예약 취소,거부 (상업자)
     */
    public void reservationCancelBusiness(int reservationIdx, Integer itemidx, Integer number, String name, String phone) {
    	itemRepository.reservationCancelBusiness(reservationIdx,itemidx,number);
    	String content = "[재고30]\n" + name + "님 상업자로부터 예약을 거절당하셨습니다.\n";
    	sendMessage(phone, content);
    }
	
	/*
	 * 인증 메시지
	 */
	public SingleMessageSentResponse sendMessage(String phone, String content) {
		
		DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCS9UG2XED3DLI5I", "TZKJX9RAOQBJO4AW3AWH1HJII4FVV83S", "https://api.coolsms.co.kr");
		
		Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01077359350");
        message.setTo(phone);
        message.setText(content);

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        log.info("sendMessageResponse={}", response);

        return response;
	}
	
	
	//----------------------------------------------------------------------------------------------------------
	
	
	/*
	 * 아이템등록시간과 현재 시간의 차이. (몇시간 전에 올라온 아이템인지)
	 */
	private int getHowBefore(Timestamp regisdate) {
		//MySql의 Timestamp의 타임존을 반영
		Timestamp newRegisdate = new Timestamp(regisdate.getTime() - (9 * 60 * 60 * 1000));
		
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
        Timestamp current = new Timestamp(now.getTime());
        
        long before = (current.getTime() - newRegisdate.getTime())/1000/60/60;
        
        return (int)before;
	}
}