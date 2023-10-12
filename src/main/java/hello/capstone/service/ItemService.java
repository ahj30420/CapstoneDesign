package hello.capstone.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import hello.capstone.dto.Alarm;
import hello.capstone.dto.Item;
import hello.capstone.dto.Member;
import hello.capstone.dto.Reservation;
import hello.capstone.dto.Shop;
import hello.capstone.exception.QuantityException;
import hello.capstone.exception.SaveItemException;
import hello.capstone.exception.SaveShopException;
import hello.capstone.exception.TimeSettingException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.ItemRepository;
import hello.capstone.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
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
	
	/*
	 * 아이템 등록
	 */
	public boolean itemsave(Item item, String method) {
		
		if(method.equals("register")) {
			Optional.ofNullable(itemRepository.findByShopIdx_itemname(item.getShopidx(), item.getItemname()))
				.ifPresent(user->{
					throw new SaveItemException(ErrorCode.DUPLICATED_ITEM,null);
				});
		}
		
		
		//MySql의 Timestamp는 타임존을 반영하기 때문에 9시간 전으로 저장이 됨. 그걸 맞추기위해 9시간을 더해줌
		Timestamp startTimeForSeoul = new Timestamp(item.getStarttime().getTime() + (9 * 60 * 60 * 1000));
		Timestamp endTimeForSeoul = new Timestamp(item.getEndtime().getTime() + (9 * 60 * 60 * 1000));
		item.setStarttime(startTimeForSeoul);
		item.setEndtime(endTimeForSeoul);
		
		int timeOut = startTimeForSeoul.compareTo(endTimeForSeoul);
		if(timeOut >= 0) {
			throw new TimeSettingException(ErrorCode.TIME_SETTING_ERROR,null);
		}
		
		log.info("service_item = {}", item);
		
		itemRepository.saveitem(item, method);
		
		itemRepository.pushAlarm(item.getShopidx());
		
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
	 * 알림 가져오기
	 */
	public List<Alarm> getAlarm(int memberidx){
		return itemRepository.getAlarm(memberidx);
	}
	
	/*
	 * 읽은 알람 삭제
	 */
	public void deleteReadAlarm(Shop shop, Member member) {
		itemRepository.deleteReadAlarm(shop, member);
	}
	
	
	
	/*
	 * 1분마다 실행되는 cron표현식 item들에 마감시간을 확인하여 시간이 지나면 자동 삭제
	 */
	@Scheduled(cron ="0 * * * * *")
	public void deleteItemEndtime() {
		log.info("item @Scheduled 실행");
		LocalDateTime now = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(now);

        // 현재 시간보다 이전인 아이템 삭제
		itemRepository.checkTrust(timestamp);
		itemRepository.deleteItemEndtime(timestamp);
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
	public void reservationCancel(int ridx, int itemidx, int number, String name, String phone) {
		itemRepository.reservationCancel(ridx, itemidx, number);
		
		String content = "[재고30]\n" + name + "님 정상적으로 예약이 취소되었습니다.\n";
		sendMessage(phone, content);
	}
	
	/*
	 * 인증 메시지
	 */
	public SingleMessageSentResponse sendMessage(String phone, String content) {
		
		DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCSRAEZQIIMYGVDM", "Z8VBFEFGTR9FIY47NF42GEK8UUKCKUKG", "https://api.coolsms.co.kr");
		
		Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01092592260");
        message.setTo(phone);
        message.setText(content);

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        log.info("sendMessageResponse={}", response);

        return response;
	}
	
}