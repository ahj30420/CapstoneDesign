package hello.capstone.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
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
import hello.capstone.exception.ValidationException;
import hello.capstone.service.ItemService;
import hello.capstone.service.ShopService;
import hello.capstone.validation.group.SaveItemValidationGroup;
import hello.capstone.validation.group.UpdateItemValidationGroup;
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
   private final MessageSource messageSource;  
   
   /*
    * 리팩토링 전 등록, 수정 // 한 메소드에서 두개의 기능을 가지고 있음.(기본에 충실하지 못했음), 여러가지 필요없는 지저분 코드가 많고 컨트롤러가 무거워짐.
    */
   
   
   /*
    * 아이템 등록
    */
//   @PostMapping("/register")
//   public Item ItemRegistration(@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
//           				   @RequestParam(value = "itemidx", defaultValue = "0") String iidx,
//                           @RequestParam("shopidx") String sid,
//                           @RequestParam("itemName") String itemname,
//                           @RequestParam("cost") String ct,
//                           @RequestParam("salecost") String sct,
//                           @RequestParam("quantity") String qt,
//                           @RequestParam("category") String category,
//                           @RequestParam("itemnotice") String itemnotice,
//                           @RequestParam("endtime") String et,
//                           @RequestParam("starttime") String st,
//                           @RequestParam(value = "existingImage", required = false) String existingImage,
//                           @RequestParam(value = "method", defaultValue = "register") String method,
//                           HttpSession session
//                           ) throws IllegalStateException, IOException, ParseException {
//      
//      
//      
//      int itemidx = Integer.parseInt(iidx);
//      int shopidx = Integer.parseInt(sid);
//      int cost = Integer.parseInt(ct);
//      int salecost = Integer.parseInt(sct);
//      int quantity = Integer.parseInt(qt);
//      
//      Item item = new Item();
//      item.setItemidx(itemidx);
//      item.setShopidx(shopidx);
//      item.setItemname(itemname);
//      item.setItemnotice(itemnotice);
//      item.setCost(cost);
//      item.setSalecost(salecost);
//      item.setQuantity(quantity);
//      item.setCategory(category);
//
//
//      Timestamp starttime = convertStringToTimestamp(st);
//
//      Timestamp endtime = convertStringToTimestamp(et);
//      
//      item.setStarttime(starttime);
//      item.setEndtime(endtime);
//
//      if(imageFile != null) {
//         String fullPath = fileDir + imageFile.getOriginalFilename();
//         imageFile.transferTo(new File(fullPath));
//         item.setImage(imageFile.getOriginalFilename());
//      }
//      else {
//    	  item.setImage(existingImage);
//      }
//
//
//      
//      itemService.itemsave(item, method);
//        
//      return item;
//   }
   

   /*
    * 리팩토링 후 상품 등록, 수정 -> 등록 및 수정을 분리하고 지저분한 코드를 최소화했으며, 연결을 담당하는 컨트롤러가 너무 무거워지지 않도록 Service에서 더 많은 로직을 처리
    */
   
   /*
    * item 등록
    */
   @PostMapping("/create")
   public void itemCreate(@Validated(value = SaveItemValidationGroup.class) @ModelAttribute Item item, BindingResult bindingResult,
                        @RequestParam("startParam") String startParam,
                        @RequestParam("endParam") String endParam) throws IllegalStateException, IOException, ParseException{
      
      if(bindingResult.hasErrors()) {
    	  sendErrors(bindingResult);
      }
      
      item.setStarttime(convertStringToTimestamp(startParam));
      item.setEndtime(convertStringToTimestamp(endParam));
      
      itemService.saveItem(item);
   }
   
   /*
    * 아이템 수정
    */
   @PutMapping("/update")
   public void itemUpdate(@Validated(value = UpdateItemValidationGroup.class) @ModelAttribute Item item, BindingResult bindingResult, 
                          @RequestParam(value = "imageF", required = false) MultipartFile imageFile,
                          @RequestParam(value = "endParam", required = false) String endParam,
                          @RequestParam(value = "startParam", required = false) String startParam) 
                             throws ParseException, IllegalStateException, IOException {

      if(bindingResult.hasErrors()) {
    	  sendErrors(bindingResult);
      }
      Item oldItem = itemService.findByItemIdx(item.getItemidx());
      
	  if(endParam != null || endParam =="" ) {
	     oldItem.setEndtime(convertStringToTimestamp(endParam));
	  }
	  if(startParam != null || startParam =="" ) {
    	 oldItem.setStarttime(convertStringToTimestamp(startParam));
	  }
      itemService.updateItem(item, imageFile, oldItem);
   }
   
   //검증 오류
   private void sendErrors(BindingResult bindingResult) {
	   Map<String, String> errors = new HashMap<>();
       for (FieldError error : bindingResult.getFieldErrors()) {
    	   String em = messageSource.getMessage(error, Locale.getDefault());
           errors.put(error.getField(), em);
       }
       throw new ValidationException(errors);
   }
   
   /*
    * 아이템 삭제
    */
   @DeleteMapping("/delete")
   public void itemDelete(@ModelAttribute Item item) {
	   itemService.itemDelete(item);
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
    * 상품 예약
    */
   @PostMapping("/reservation")
   public void reservation(@RequestParam("shopidx") int shopIdx,
	   				       @RequestParam("memberidx") int memberIdx,
	   					   @RequestParam("itemidx") int itemIdx,
	   					   @RequestParam("number") int number,
	   					   @RequestParam("shopname") String shopname,
	   					   @RequestParam("itemname") String itemname,
	   					   @RequestParam("name") String name,
	   					   @RequestParam("phone") String phone) {
	   
	   Reservation reservation = new Reservation(0,memberIdx,shopIdx,itemIdx,number,null,"wait");
	   
	   itemService.reservation(reservation, shopname, itemname, name, phone);
	   
   }
   
   /*
    *  상품 예약 확인(상업자가 확인 버튼 클릭)
    */
   @PostMapping("/reservation/confirm")
   public String confirm(@RequestParam("reservationidx") int reservationIdx) {
	   itemService.reservationConfirm(reservationIdx);
	   
	   return "";
   }
   
   /*
    * 상품 예약 취소(사용자)
    */
   @PostMapping("/reservation/cancel")
   public void cancel(HttpSession session, @RequestBody List<Map<String, Object>> reservationinfo) {
	   Member member = (Member) session.getAttribute("member");
	   String phone = member.getPhone();
	   String name = member.getName();
	   
	   itemService.reservationCancel(reservationinfo, name, phone);
   }
   
   /*
    * 예약 상품 리스트 조회(사용자) (대기중인 예약, 완료된 예약 따로)
    */
   @GetMapping("/reservation/getreservations")
   public List<Map<String, Object>> getReservations(@RequestParam("confirm") String confirm,HttpSession session){
	   Member member = (Member) session.getAttribute("member");
	   
	   return itemService.getReservations(member.getMemberIdx(), confirm);
   }
   
   /*
    * 상품 예약 취소(상업자)
    */
   @DeleteMapping("/reservation/cancel/business")
   public void reservationCancelBusiness(@RequestParam("reservationIdx") Integer reservationIdx) {
	   itemService.reservationCancelBusiness(reservationIdx);
	   
   }
   
   
   
   //---------------------------------------------------------------------------------------------------
   
	
   //String을 Timestamp로 변환하는 함수
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