package hello.capstone.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hello.capstone.dto.Item;
import hello.capstone.dto.Member;
import hello.capstone.dto.Notice;
import hello.capstone.dto.Reservation;
import hello.capstone.dto.Shop;

@Mapper
public interface ManagerMapper {
	
	//공지사항------------------------------------------------------------------------------------------
	
	//공지사항 등록
	void noticeCreate(Notice notice);
	
	//공지사항 읽기
	Notice noticeRead(@Param("noticeIdx") int noticeIdx, @Param("title") String title);
	
	//공지사항 수정
	void noticeUpdate(Notice newNotice);
	
	//공지사항 삭제
	void noticeDelete(Notice notice);

	//모든공지사항 읽기
	List<Notice> noticeReadAll();
	
	
	
	//사용자 관리---------------------------------------------------------------------------------------
	
	//역할 별 사용자 조회
	List<Member> getMemberByRole(String role);
	
	//실패한(신뢰도가 깍인 예약조회)
	List<Reservation> getFailedReservation(int memberIdx);
	
	//신뢰도가 깎인 가게에서 예약한 상품
	List<Item> getFailedItems(int shopIdx);
	
	
	//상업자 관리---------------------------------------------------------------------------------------
	
	//해당 상업자의 가게 정보 조회
	List<Shop> getShopinfoByBusiness(int owneridx);
	
	//해당 가게에 등록했던 상품 조회
	List<Item> getIteminfoByBusiness(int shopidx);
	
	
	
	
	//가게 분석-----------------------------------------------------------------------------------------

	//모든 가게 정보 조회
	List<Shop> getShopinfo();
	
	//해당 가게에 등록된 상품과 상품별 예약자 수 조회(1)
	List<Map<String, Object>> getIteminfo(int shopidx);
	
	//해당 가게에 등록된 상품과 상품별 예약자 수 조회(2) -> 예약자 수 클릭시 예약자 정보와 예약한 상품 수, 구매 확정 여부 표시
	public List<Map<String, Object>> getReservationClient(int itemidx);
	
	//별점 카테고리(0,1,2,3,4,5) 별 인원수
	public List<Map<String, Object>> getRatingNumber(int shopidx);
	
	//별점 카테고리(0,1,2,3,4,5) 별 인원수(2) -> 인원수 클릭시 해당 별점을 입력했던 사용자 정보 표시
	public List<Map<String, Object>> getRatingClient (int shopidx, int rating);
	
	//해당 가게에서 상품을 구매해간 고객 정보
	List<Member> getReservationMember(int shopidx);
	
	
	
    //검색---------------------------------------------------------------------------------------------
	
	//모든 아이템 나열, 가게이름 포함
	List<Map<String, Object>> getItemAll();
	
	//이름으로 회원 검색 - 이름 -> 날짜 순 정렬
	List<Member> searchMemberByName(String name);
	
	//이름으로 가게 검색 - 이름 -> 날짜 순 정렬 , 가게 주인 포함
	List<Map<String , Object>> searchShopByName(String shopName);
	
	//이름으로 아이템 검색 - 이름 -> 날짜 순 정렬, 가게 포함
	List<Map<String, Object>> searchItemByName(String itemName);
	
}