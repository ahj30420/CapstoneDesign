package hello.capstone.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hello.capstone.dto.Item;
import hello.capstone.dto.Member;
import hello.capstone.dto.Notice;
import hello.capstone.dto.Reservation;
import hello.capstone.dto.Shop;
import hello.capstone.exception.AlreadyBookmarkedShopException;
import hello.capstone.exception.NullContentException;
import hello.capstone.exception.NullTitleException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.ItemRepository;
import hello.capstone.repository.ManagerRepository;
import hello.capstone.repository.MemberRepository;
import hello.capstone.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {
	
	private final ManagerRepository managerRepository;
	private final ShopRepository shopRepository;

	
	/*
    * 공지사항 CREATE
    */
   public void noticeCreate(Notice notice) {
      
      if(notice.getTitle() == null || notice.getTitle().isEmpty()) {
         throw new NullTitleException(ErrorCode.NULL_TITLE,null);
      }
      if(notice.getContent() == null || notice.getTitle().isEmpty()) {
         throw new NullContentException(ErrorCode.NULL_CONTENT,null);
      }
      
      LocalDateTime dateTime = LocalDateTime.now();
      Timestamp timestamp = Timestamp.valueOf(dateTime);
      //MySql의 타임존을 반영하여 9시간을 더해줌.
      Timestamp now = new Timestamp(timestamp.getTime() + (9 * 60 * 60 * 1000));
      notice.setNoticeDate(now);
      
      managerRepository.noticeCreate(notice);
   }
   
   /*
    * 공지사항 READ
    */
   public Map<String,Object> noticeRead(int noticeIdx){
      return managerRepository.noticeRead(noticeIdx);
   }
   
   
   /*
    * 공지사항 UPDATE
    */
   public void noticeUpdate(Notice newNotice) {
      if(newNotice.getTitle() == null || newNotice.getTitle().isEmpty()) {
         throw new NullTitleException(ErrorCode.NULL_TITLE,null);
      }
      if(newNotice.getContent() == null || newNotice.getTitle().isEmpty()) {
         throw new NullContentException(ErrorCode.NULL_CONTENT,null);
      }
      
      LocalDateTime dateTime = LocalDateTime.now();
      Timestamp timestamp = Timestamp.valueOf(dateTime);
      //MySql의 타임존을 반영하여 9시간을 더해줌.
      Timestamp now = new Timestamp(timestamp.getTime() + (9 * 60 * 60 * 1000));
      
      newNotice.setNoticeModify("수정됨");
      newNotice.setNoticeDate(now);

      managerRepository.noticeUpdate(newNotice);
   }
   
   /*
    * 공지사항 DELETE
    */
   public void noticeDelete(int noticeIdx) {
      managerRepository.noticeDelete(noticeIdx);
   }
   
   /*
    * 모든 공지사항 READ
    */
   public List<Map<String, Object>> noticeReadAll(){
      List<Map<String, Object>> notices = managerRepository.noticeReadAll();
      
      for(Map<String, Object> notice : notices) {
         Timestamp newRegisdate = new Timestamp(((Timestamp) notice.get("noticedate")).getTime() - (9 * 60 * 60 * 1000));
         notice.put("noticedate", newRegisdate);
      }
      return notices;
   }
   
   /*
    * 공지사항 알림
    */
   public List<Map<String, Object>> noticeGetAlarm(){
      List<Map<String, Object>> notices = managerRepository.noticeGetAlarm();
      //24시간 이전에 올라온 공지사항만 가져오기
      for(Map<String, Object> notice : notices) {
         
         LocalDateTime dateTime = LocalDateTime.now();
         Timestamp timestamp = Timestamp.valueOf(dateTime);
         //MySql의 타임존을 반영하여 9시간을 더해줌.
         Timestamp now = new Timestamp(timestamp.getTime() + (9 * 60 * 60 * 1000));
         //시간단위로 차이 구하기
           int before = (int) ((now.getTime() - ((Timestamp)notice.get("noticedate")).getTime()) / (1000 * 60 * 60));
           
           notice.put("before", before);
      }
      return notices;
   }

	//사용자 관리---------------------------------------------------------------------------------------
	
	/*
     * 역할별  사용자 조회, 가나다 순
     */
	public List<Member> getMemberByRole(String role){
		return managerRepository.getMemberByRole(role);
	}
	
	/*
    * 실패한 예약 조회(신뢰도가 깎인 예약) 깎은 가게와 횟수
    */
   public List<Map<String, Object>> getFailedReservation(int memberIdx){
      List<Map<String, Object>> failedReservations = managerRepository.getFailedReservation(memberIdx);

      return failedReservations;
   }
   
   /*
    * 신뢰도를 깎은 가게에서 어떤 아이템을 예약했었는지 조회
    */
   public List<Map<String, Object>> getFailedItems(int shopIdx, int memberIdx){
      
      return managerRepository.getFailedItems(shopIdx, memberIdx);
   }
	
	
	
	//상업자 관리---------------------------------------------------------------------------------------
	
	/*
	 *  해당 상업자의 가게 정보 조회
	 */
	public List<Shop> getShopinfoByBusiness(int owneridx){
		return managerRepository.getShopinfoByBusiness(owneridx);
	}
	
	/*
	 * 해당 가게에 등록했던 상품 조회
	 */
	public List<Item> getIteminfoByBusiness(int shopidx){
		return managerRepository.getIteminfoByBusiness(shopidx);
	}
	
	
	
	//가게 분석---------------------------------------------------------------------------------------

	/*
	 * 모든 가게 정보 조회
	 */
	public List<Map<String, Object>> getShopinfo(){
		return managerRepository.getShopinfo();
	}
	
	/*
	 * 해당 가게에 등록된 상품과 상품별 예약자 수 조회(1)
	 */
	public List<Map<String, Object>> getIteminfo(int shopidx){
		List<Map<String, Object>> iteminfo = managerRepository.getIteminfo(shopidx);
		return iteminfo;
	}
	
	/*
	 * 별점 카테고리(0,1,2,3,4,5) 별 인원수
	 */
	public List<Map<String, Object>> getRatingNumber(int shopidx){
		return managerRepository.getRatingNumber(shopidx);
	}
	
	/*
	 * 별점 카테고리(0,1,2,3,4,5) 별 인원수(2) -> 인원수 클릭시 해당 별점을 입력했던 사용자 정보 표시
	 */
	public List<Map<String, Object>> getRatingClient (int shopidx, int rating){
		return managerRepository.getRatingClient(shopidx, rating);
	}

	/*
	 * 해당 가게에서 상품을 구매해간 고객 정보 
	 */
	public List<Map<String, Object>> getReservationMember(int shopidx){
		return managerRepository.getReservationMember(shopidx);
	}
	
}