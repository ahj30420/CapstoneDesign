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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    * 아이템 등록
    */
   @PostMapping("/register")
   public Item ItemRegistration(@RequestParam(value = "image", required = false) MultipartFile Image,
           				   @RequestParam(value = "itemidx", defaultValue = "0") String iidx,
                           @RequestParam("shopidx") String sid,
                           @RequestParam("itemName") String itemname,
                           @RequestParam("cost") String ct,
                           @RequestParam("salecost") String sct,
                           @RequestParam("quantity") String qt,
                           @RequestParam("category") String category,
                           @RequestParam("itemnotice") String itemnotice,
                           @RequestParam("endtime") String et,
                           @RequestParam("starttime") String st,
                           @RequestParam(value = "existingImage", required = false) String existingImage,
                           @RequestParam(value = "method", defaultValue = "register") String method,
                           HttpSession session
                           ) throws IllegalStateException, IOException, ParseException {
      
      log.info("shopidx = {}", sid);
      
      int itemidx = Integer.parseInt(iidx);
      int shopidx = Integer.parseInt(sid);
      int cost = Integer.parseInt(ct);
      int salecost = Integer.parseInt(sct);
      int quantity = Integer.parseInt(qt);
      
      Item item = new Item();
      item.setItemidx(itemidx);
      item.setShopidx(shopidx);
      item.setItemname(itemname);
      item.setItemnotice(itemnotice);
      item.setCost(cost);
      item.setSalecost(salecost);
      item.setQuantity(quantity);
      item.setCategory(category);
      
      log.info("starttime = {}", st);
      Timestamp starttime = convertStringToTimestamp(st);
      log.info("convertStringToTimestamp = {}", starttime);
      Timestamp endtime = convertStringToTimestamp(et);
      
      item.setStarttime(starttime);
      item.setEndtime(endtime);
      log.info("image = {} ",Image);
      if(Image != null) {
         String fullPath = fileDir + Image.getOriginalFilename();
         Image.transferTo(new File(fullPath));
         log.info("fullPath = {}", fullPath);
         item.setImage(Image.getOriginalFilename());
      }
      else {
    	  item.setImage(existingImage);
      }
      
      log.info("item = {}", item);
      log.info("method = {}", method);
      
      itemService.itemsave(item, method);
        
      return item;
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
   public String reservation(@RequestParam("shopidx") String si,
		   					 @RequestParam("memberidx") String mi,
		   					 @RequestParam("itemidx") String ii,
		   					 @RequestParam("number") String num,
		   					 @RequestParam("shopname") String shopname,
		   					 @RequestParam("itemname") String itemname,
		   					 @RequestParam("name") String name,
		   					 @RequestParam("phone") String phone) {
	   
	   int memberidx = Integer.parseInt(mi);
	   int shopidx = Integer.parseInt(si);
	   int itemidx = Integer.parseInt(ii);
	   int number = Integer.parseInt(num);
	   
	   Reservation reservation = new Reservation(0,memberidx,shopidx,itemidx,number,null,"wait");
	   
	   itemService.reservation(reservation, shopname, itemname, name, phone);
	   return "";
   }
   
   /*
    *  상품 예약 확인(상업자가 확인 버튼 클릭)
    */
   @PostMapping("/reservation/confirm")
   public String confirm(@RequestParam("reservationidx") String ridx) {
	   
	   int reservationidx = Integer.parseInt(ridx);
	   
	   itemService.reservationConfirm(reservationidx);
	   
	   return "";
   }
   
   /*
    * 상품 예약 취소
    */
   @PostMapping("/reservation/cancel")
   public String cancel(@RequestParam("reservationidx") String ridx,
		   				@RequestParam("name") String name,
		   				@RequestParam("phone") String phone,
		   				@RequestParam("itemidx") String iidx,
		   				@RequestParam("number") String num) {
	   
	   int reservationidx = Integer.parseInt(ridx);
	   int itemidx = Integer.parseInt(iidx);
	   int number = Integer.parseInt(num);
	   
	   itemService.reservationCancel(reservationidx, itemidx, number, name, phone);
	   
	   return "";
   }
   
   
   /*
    * String을 Timestamp로 변환하는 함수
    */
   private Timestamp convertStringToTimestamp(String dateString) throws ParseException {
	   SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
       
	   Date parsedDate = inputDateFormat.parse(dateString);
	   
       return new Timestamp(parsedDate.getTime());
   }
}