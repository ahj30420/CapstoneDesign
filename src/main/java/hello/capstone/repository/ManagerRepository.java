package hello.capstone.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import hello.capstone.dto.Item;
import hello.capstone.dto.Member;
import hello.capstone.dto.Notice;
import hello.capstone.dto.Reservation;
import hello.capstone.dto.Shop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ManagerRepository {

	private final ManagerMapper managerMapper;

	/*
    * 공지사항 CREATE
    */
   public void noticeCreate(Notice notice) {
      managerMapper.noticeCreate(notice);
   }
   
   /*
    * 공지사항 READ
    */
   public Map<String,Object> noticeRead(int noticeIdx) {
      return managerMapper.noticeRead(noticeIdx);
   }
   
   /*
    * 공지사항 UPDATE
    */
   public void noticeUpdate(Notice newNotice) {
      managerMapper.noticeUpdate(newNotice);
   }
   
   /*
    * 공지사항 DELETE
    */
   public void noticeDelete(int noticeIdx) {
      managerMapper.noticeDelete(noticeIdx);
   }
   
   /*
    * 공지사항 READ
    */
   public List<Map<String, Object>> noticeReadAll(){
      return managerMapper.noticeReadAll();
   }
   
   /*
    * 공지사항 알림
    */
   public List<Map<String, Object>> noticeGetAlarm(){
      return managerMapper.noticeGetAlarm();
   }
	
	//사용자 관리---------------------------------------------------------------------------------------
	
	/*
    * 역할 별 사용자 조회
    */
	public List<Member> getMemberByRole(String role){
		return managerMapper.getMemberByRole(role);
	}
	
	/*
    * 실패한 예약 조회(신뢰도가 깎인 예약) 깎은 가게와 횟수
    */
   public List<Map<String, Object>> getFailedReservation(int memberIdx){
      return managerMapper.getFailedReservation(memberIdx);
   }
   
   /*
    * 신뢰도가 깎인 가게에서 예약한 상품
    */
   public List<Map<String, Object>> getFailedItems(int shopIdx, int memberIdx){
      return managerMapper.getFailedItems(shopIdx, memberIdx);
   }
	
	
	
	//상업자 관리---------------------------------------------------------------------------------------
	
	/*
	 *  해당 상업자의 가게 정보 조회
	 */
	public List<Shop> getShopinfoByBusiness(int owneridx){
		return managerMapper.getShopinfoByBusiness(owneridx);
	}
	
	/*
	 * 해당 가게에 등록했던 상품 조회
	 */
	public List<Item> getIteminfoByBusiness(int shopidx){
		return managerMapper.getIteminfoByBusiness(shopidx);
	}
	
	
	
	//가게 분석-----------------------------------------------------------------------------------------

	/*
	 * 모든 가게 정보 조회
	 */
	public List<Map<String, Object>> getShopinfo(){
		return managerMapper.getShopinfo();
	}
	
	/*
	 * 해당 가게에 등록된 상품과 상품별 예약자 수 조회(1)
	 */
	public List<Map<String, Object>> getIteminfo(int shopidx){
		List<Map<String, Object>> iteminfo = managerMapper.getIteminfo(shopidx);
		return iteminfo;
	}
	
	/*
	 * 별점 카테고리(0,1,2,3,4,5) 별 인원수
	 */
	public List<Map<String, Object>> getRatingNumber(int shopidx){
		return managerMapper.getRatingNumber(shopidx);
	}
	
	/*
	 * 별점 카테고리(0,1,2,3,4,5) 별 인원수(2) -> 인원수 클릭시 해당 별점을 입력했던 사용자 정보 표시
	 */
	public List<Map<String, Object>> getRatingClient (int shopidx, int rating){
		return managerMapper.getRatingClient(shopidx, rating);
	}
	
	/*
	 * 해당 가게에서 상품을 구매해간 고객 정보 
	 */
	public List<Map<String, Object>> getReservationMember(int shopidx){
		return managerMapper.getReservationMember(shopidx);
	}
	
}