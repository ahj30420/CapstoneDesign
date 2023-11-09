package hello.capstone.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.capstone.dto.Item;
import hello.capstone.dto.Member;
import hello.capstone.dto.Notice;
import hello.capstone.dto.Shop;
import hello.capstone.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/*
 * 관리자 모드, 회원관리, 가게관리, 상품관리, 공지사항 등등
 */

@Slf4j
@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
	
	private final ManagerService managerService;
	
   //공지사항------------------------------------------------------------------------------------------
	   
   /*
    * 공지사항 CREATE
    */
   @PostMapping("/notice/create")
   public void noticeCreate(@ModelAttribute Notice notice) {
      managerService.noticeCreate(notice);
      
   }
   
   /*
    * 공지사항 READ 
    */
   @GetMapping("/notice/read")
   public Map<String,Object> noticeRead(@RequestParam int noticeIdx ){

      return managerService.noticeRead(noticeIdx);
   }
   
   /*
    * 공지사항 UPDATE
    */
   @PutMapping("/notice/update")
   public Map<String,Object> noticeUpdate(@ModelAttribute Notice newNotice) {
      managerService.noticeUpdate(newNotice);
      return noticeRead(newNotice.getNoticeIdx());
   }
   
   /*
    * 공지사항 DELETE
    */
   @DeleteMapping("/notice/delete")
   public void noticeDelete(@RequestParam int noticeIdx) {
      managerService.noticeDelete(noticeIdx);
   }
   
   /*
    * 모든 공지사항 READ 
    */
   @GetMapping("/notice/readall")
   public List<Map<String, Object>> noticeReadAll(){
      
      return managerService.noticeReadAll();
   }
   
   /*
    * 공지사항 알림 가져오기
    */
   @GetMapping("/notice/getalarm")
   public List<Map<String, Object>> getNoticeAlarm(){
      List<Map<String, Object>> notices = managerService.noticeGetAlarm();

      
      return notices;
   }
   


	//사용자 관리---------------------------------------------------------------------------------------
	/*
	    * 일반 사용자 조회
	    */
	   @GetMapping("/member/user")
	   public List<Member> getUserMember(){
	      
	      return managerService.getMemberByRole("사용자");
	   }
	   
	   /*
	    * 신뢰도를 깎은 가게와 예약 날짜 조회
	    */
	   @GetMapping("/member/user/trustmanage")
	   public List<Map<String, Object>> trustManage(@RequestParam("memberIdx") int memberIdx){      
	      
	      return managerService.getFailedReservation(memberIdx);
	   }
	   
	   /*
	    * 신뢰도를 깎은 가게에서 어떤 아이템을 예약했었는지 조회
	    */
	   @GetMapping("/member/user/trustmanage/item")
	   public List<Map<String, Object>> failItem(@RequestParam("shopidx") int shopIdx, @RequestParam("memberidx") int memberIdx  ){
	   
	      return managerService.getFailedItems(shopIdx,memberIdx);
	   }

	
	
	//상업자 관리---------------------------------------------------------------------------------------
	
	/*
	 * 상업자 맴버 조회 
	 */
	@GetMapping("/member/business")
	public List<Member> getBusinessMember() {
		
		return managerService.getMemberByRole("상업자");
	}
	
	/*
	 * 해당 상업자의 가게 정보 조회
	 */
	@GetMapping("/member/business/shopinfo")
	public List<Shop> getShopinfoByBusiness(@RequestParam("memberidx") int owneridx){
		
		return managerService.getShopinfoByBusiness(owneridx);
	}
	
	/*
	 * 해당 가게에 등록했던 상품 조회
	 */
	@GetMapping("/member/business/iteminfo")
	public List<Item> getIteminfoByBusniess(@RequestParam("shopidx") int shopidx){
		return managerService.getIteminfoByBusiness(shopidx);
	}
	
	
	
	//관리자 관리---------------------------------------------------------------------------------------
	
	/*
	 *  관리자 맴버 조회 
	 */
	@GetMapping("/member/admin")
	public List<Member> getAdminMember(){
		
		return managerService.getMemberByRole("관리자");
	}
	
	
	
	//가게 분석---------------------------------------------------------------------------------------
	
	/*
	 * 모든 가게 정보 조회
	 */
	@GetMapping("/analysis/shop")
	public List<Map<String, Object>> getShopinfo(){
		return managerService.getShopinfo();
	}
	
	/*
	 * 해당 가게에 등록된 상품과 상품별 예약자 수 조회(1)
	 */
	@GetMapping("/analysis/item")
	public List<Map<String, Object>> AnalysisItem(@RequestParam("shopidx") int shopidx){
		return managerService.getIteminfo(shopidx);
	}

	/*
	 * 별점 카테고리(0,1,2,3,4,5) 별 인원수(1)
	 */
	@GetMapping("/analysis/rating")
	public List<Map<String, Object>> AnalysisRating(@RequestParam("shopidx") int shopidx){
		return managerService.getRatingNumber(shopidx);
	}
	
	/*
	 * 별점 카테고리(0,1,2,3,4,5) 별 인원수(2) -> 인원수 클릭시 해당 별점을 입력했던 사용자 정보 표시
	 */
	@GetMapping("/analysis/rating/client")
	public List<Map<String, Object>> AnalysisRatingClient(@RequestParam("shopidx") int shopidx,
														  @RequestParam("rating") int rating){
		return managerService.getRatingClient(shopidx, rating);
	}
	
	/*
	 * 해당 가게에서 상품을 구매해간 고객 정보 
	 */
	@GetMapping("/analysis/reservation/member")
	public List<Map<String, Object>> AnalysisReservation(@RequestParam("shopidx") int shopidx){
		return managerService.getReservationMember(shopidx);
	}

}