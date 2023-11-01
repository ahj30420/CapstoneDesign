 package hello.capstone.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hello.capstone.dto.Item;
import hello.capstone.dto.Member;
import hello.capstone.dto.Reservation;
import hello.capstone.dto.Shop;
import hello.capstone.service.ItemService;
import hello.capstone.service.ShopService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/item")
@RestController
@RequiredArgsConstructor
public class ItemController {

   private final ItemService itemService;
   private final ShopService shopService;
   
   @Value("${itemfile.dir}")
   private String fileDir;
   
   /*
    * item 등록
    */
   @PostMapping("/create")
   public void itemCreate(@RequestParam("imageFile") MultipartFile imageFile,
			              @RequestParam("shopidx") int shopIdx,
			              @RequestParam("itemName") String itemName,
			              @RequestParam("cost") int cost,
			              @RequestParam("salecost") int saleCost,
			              @RequestParam("quantity") int quantity,
			              @RequestParam("category") String category,
			              @RequestParam("itemnotice") String itemNotice,
			              @RequestParam("endtime") String endParam,
			              @RequestParam("starttime") String startParam) 
			            		  throws IllegalStateException, IOException, ParseException{
	   
	   Item item = new Item();
	   
	   item.setShopidx(shopIdx);
	   item.setItemname(itemName);
	   item.setImageFile(imageFile);
	   item.setCost(cost);
	   item.setQuantity(quantity);
	   item.setCategory(category);
	   item.setItemnotice(itemNotice);
	   item.setSalecost(saleCost);
	   item.setStarttime(convertStringToTimestamp(startParam));
	   item.setEndtime(convertStringToTimestamp(endParam));
	   
	   itemService.saveItem(item);
   }
   
   
   /*
    * 아이템 수정
    */
   @PutMapping("/update")
   public void itemUpdate(@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
			              @RequestParam("itemidx") int itemIdx,
			              @RequestParam("itemName") String itemName,
			              @RequestParam("cost") int cost,
			              @RequestParam("salecost") int saleCost,
			              @RequestParam("quantity") int quantity,
			              @RequestParam("category") String category,
			              @RequestParam("itemnotice") String itemNotice,
			              @RequestParam(value = "endtime", required = false) String endParam,
			              @RequestParam(value = "starttime", required = false) String startParam) 
			            		  throws ParseException, IllegalStateException, IOException {
	   
	   Item oldItem = itemService.findByItemIdx(itemIdx);

	   oldItem.setItemname(itemName);
	   oldItem.setCost(cost);
	   oldItem.setSalecost(saleCost);
	   oldItem.setQuantity(quantity);
	   oldItem.setCategory(category);
	   oldItem.setItemnotice(itemNotice);
	   log.info("endparam = {}", endParam);
	   if(endParam != null || endParam =="" ) {
		   oldItem.setEndtime(convertStringToTimestamp(endParam));
	   }
	   if(startParam != null || startParam =="" ) {
		   oldItem.setStarttime(convertStringToTimestamp(startParam));
	   }
	   
	   
	   itemService.updateItem(oldItem, imageFile);
   }
   
   /*
    * 아이템 정보가져오기
    */
   @PostMapping("/getItems")
   public List<Item> getItems(@RequestBody Shop shop) {
      
      int shopIdx = shopService.getShopIdx(shop);
      
      List<Item> items = itemService.getItems(shopIdx);
      
      return items;
   }
   
   /*
    * 아이템 삭제
    */
   @DeleteMapping("/delete")
   public void itemDelete(@ModelAttribute Item item) {
	   itemService.itemDelete(item);
   }
   
   /*
    * 상품 예약
    */
   @PostMapping("/reservation")
   public String reservation(HttpSession session, @RequestParam("shopidx") String si,
		   					 @RequestParam("memberidx") String mi,
		   					 @RequestParam("itemidx") String ii,
		   					 @RequestParam("number") String num,
		   					 @RequestParam("shopname") String shopname,
		   					 @RequestParam("itemname") String itemname) {
	   
	   int memberidx = Integer.parseInt(mi);
	   int shopidx = Integer.parseInt(si);
	   int itemidx = Integer.parseInt(ii);
	   int number = Integer.parseInt(num);
	   
	   log.info("itemidx={}", itemidx);
	   
	   Member member = (Member) session.getAttribute("member");
	   String name = member.getName(); 
	   String phone = member.getPhone();
	   
	   Reservation reservation = new Reservation(0,memberidx,shopidx,itemidx,number,null,"wait");
	   
	   itemService.reservation(reservation, shopname, itemname, name, phone);
	   return "";
   }
   
   /*
    * 상품 예약 확인(상업자가 확인 버튼 클릭)
    */
   @PostMapping("/reservation/confirm")
   public void reservationComplete(@RequestParam("reservationidx") String ridx) {
	   int reservationidx = Integer.parseInt(ridx);
	   itemService.reservationConfirm(reservationidx);
   }
   
   /*
    * 사용자 입장에서 상품 예약 취소
    */
   @PostMapping("/reservation/cancel")
   public String cancel(HttpSession session, @RequestBody List<Map<String, Object>> reservationinfo) {
	   
	   log.info("reservationinfo = {}", reservationinfo);
	   
	   for(Map<String, Object> info : reservationinfo) {
		   log.info("reservationidx = {}",info.get("reservationidx"));
	   }
	   
	   Member member = (Member) session.getAttribute("member");
	   String phone = member.getPhone();
	   String name = member.getName();
	   
	   log.info("phone = {}", phone);
	   log.info("name = {}", name);
	   
	   itemService.reservationCancel(reservationinfo, name, phone);
	   return "";
   }
   
   /*
    * 예약 상품 리스트 조회(사용자) (대기중인 예약, 완료된 예약 따로)
    */
   @GetMapping("/reservation/getreservations")
   public List<Map<String, Object>> getReservations(@RequestParam("confirm") String confirm,HttpSession session){
	   log.info("confirm={}",confirm);
	   Member member = (Member) session.getAttribute("member");
	   return itemService.getReservations(member.getMemberIdx(), confirm);
   }
   
   /*
    * 상품 예약 취소(상업자)
    */
   @DeleteMapping("/reservation/cancel/business")
   public void reservationCancelBusiness(HttpSession session,
		    							 @RequestParam("reservationIdx") Integer reservationIdx,
		   								 @RequestParam("itemidx") Integer itemidx,
		   								 @RequestParam("number") Integer number
		   								 ) {
	   Member member = (Member) session.getAttribute("member");
	   
	   String name = member.getName();
	   String phone = member.getPhone();
	   
	   itemService.reservationCancelBusiness(reservationIdx,itemidx,number, name, phone);
   }
   
   
   /*
    * String을 Timestamp로 변환하는 함수
    */
   private Timestamp convertStringToTimestamp(String dateString) throws ParseException {
     
	   try {
		   SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
		   Date parsedDate = inputDateFormat.parse(dateString);
		   Timestamp parseTime = new Timestamp(parsedDate.getTime());
		   //타임존 반영을 위해 9시간 +
		   Timestamp Time = new Timestamp(parseTime.getTime() + (9 * 60 * 60 * 1000));
		   
		   return Time;
       
       } catch(ParseException e) {
    	   SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    	   Date parsedDate = inputDateFormat.parse(dateString);
    	   return new Timestamp(parsedDate.getTime());
       }
	       
   }
}
