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
import hello.capstone.dto.Shop;
import hello.capstone.service.ItemService;
import hello.capstone.service.ShopService;
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
   public Item ItemRegistration(@RequestParam("image") MultipartFile Image,
                           @RequestParam("shopidx") String sid,
                           @RequestParam("itemName") String itemname,
                           @RequestParam("cost") String ct,
                           @RequestParam("salecost") String sct,
                           @RequestParam("quantity") String qt,
                           @RequestParam("category") String category,
                           @RequestParam("itemnotice") String itemnotice,
                           @RequestParam("endtime") String et,
                           @RequestParam("starttime") String st
                           ) throws IllegalStateException, IOException, ParseException {
      
	   log.info("shopidx = {}", sid);
	   
	  int shopidx = Integer.parseInt(sid);
	  int cost = Integer.parseInt(ct);
	  int salecost = Integer.parseInt(sct);
	  int quantity = Integer.parseInt(qt);
	   
      Item item = new Item();
      item.setShopidx(shopidx);
      item.setItemname(itemname);
      item.setItemnotice(itemnotice);
      item.setCost(cost);
      item.setSalecost(salecost);
      item.setQuantity(quantity);
      item.setCategory(category);
      
      Timestamp starttime = convertStringToTimestamp(st);
      Timestamp endtime = convertStringToTimestamp(et);
      
      item.setStarttime(starttime);
      item.setEndtime(endtime);
      
      if(!Image.isEmpty()) {
         String fullPath = fileDir + Image.getOriginalFilename();
         Image.transferTo(new File(fullPath));
      }
      item.setImage(Image.getOriginalFilename());
      
      log.info("item = {}", item);
      
      itemService.itemsave(item);
        
      return item;
   }
   
   /*
    * 아이템 정보가져오기
    */
   @PostMapping("/getItems")
   public List<Item> getItems(@RequestBody Shop shop) {
      log.info("shop = {} ", shop);
      int shopIdx = shopService.getShopIdx(shop);
      
      log.info("shop = {} ", shop);
      List<Item> items = itemService.getItems(shopIdx);
      
      log.info("items = {} ", items);
      return items;
   }
   
   /*
    * String을 Timestamp로 변환하는 함수
    */
   public Timestamp convertStringToTimestamp(String dateString) throws ParseException {
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       Date parsedDate = dateFormat.parse(dateString);
       return new Timestamp(parsedDate.getTime());
   }
   
//   /*
//    * 상품 예약
//    */
//   @PostMapping("/reservation")
//   public String reservation(@RequestParam String count, @RequestParam String memberidx, @RequestParam String itemidx) {
//	   
//   }
   
}








