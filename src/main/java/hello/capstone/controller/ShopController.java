package hello.capstone.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hello.capstone.dto.Member;
import hello.capstone.dto.Ratings;
import hello.capstone.dto.Shop;
import hello.capstone.exception.LogInException;
import hello.capstone.exception.ValidationException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.service.MemberService;
import hello.capstone.service.ShopService;
import hello.capstone.validation.group.SaveShopValidationGroup;
import hello.capstone.validation.group.UpdateShopValidationGroup;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ShopController {
	
	private final MemberService memberService;
	private final ShopService shopService;
	private final MessageSource messageSource;
	
	@Value("${file.dir}")
	private String fileDir;
	
	/*
	    * 가게 등록
	    */
	   @PostMapping("/shop/create")
	   public void shopCreate(@Validated(value = SaveShopValidationGroup.class) @ModelAttribute Shop shop, 
	                     BindingResult bindingResult, HttpSession session) 
	                           throws IllegalStateException, IOException {
	      if(bindingResult.hasErrors()) {
	          Map<String, String> errors = new HashMap<>();
	          for (FieldError error : bindingResult.getFieldErrors()) {
		    		String em = messageSource.getMessage(error, Locale.getDefault());
		            errors.put(error.getField(), em);
	           }
	          throw new ValidationException(errors);
	       }
	      
	      Member member = (Member) session.getAttribute("member");
	      shop.setOwnerIdx(member.getMemberIdx());
	      log.info("shop = {}", shop);
	      shopService.saveShop(shop);
	   }
	   
	   
	   /*
	    * 가게 수정 - 이미지를 변경하지 않을떄 multipartFile가 null이기 때문에 @RequestParam으로 따로따로 받음
	    */
	   @PutMapping("/shop/update")
	   public void shopUpdate(@Validated(value = UpdateShopValidationGroup.class) @ModelAttribute Shop shop, BindingResult bindingResult,
	                     @RequestParam(value = "imagefile", required = false) MultipartFile Image ) throws IllegalStateException, IOException {
	      log.info("shop = {}",shop);
	      if(bindingResult.hasErrors()) {
	          Map<String, String> errors = new HashMap<>();
	          for (FieldError error : bindingResult.getFieldErrors()) {
		    		String em = messageSource.getMessage(error, Locale.getDefault());
		            errors.put(error.getField(), em);
	           }
	          throw new ValidationException(errors);
	       }
	      
	      shopService.updateShop(shop, Image, shop.getShopAddress());
	   }
	
	@DeleteMapping("/shopDelete")
	public void shopDelete(@RequestParam("shopidx") int shopidx) {
		shopService.shopDelete(shopidx);
	}
	
	/*
	 * 본인 인증(pw 확인)
	 */
	@PostMapping("/Pw_verification")
	public String verification(@RequestParam String check_pw, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		String real_pw = member.getPw();
		
		if(real_pw.equals(check_pw)) {
			return "";
		}
		else {
			throw new LogInException(ErrorCode.PASSWORD_MISMATCH, null);
		}
	}
	
	
	/*
	 * 지도 shop marker 표시 (모든 shop)
	 */
	@GetMapping("/ShopMarker")
	public List<Shop> ShopAddress(HttpSession session){
		List<Shop> shops = shopService.getShops();
		
		return shops;
	} 
	
	/*
	 * 본인의 가게 정보 가져오기 (상업자 버전)
	 */
	@GetMapping("/getMyShop")
	public List<Shop> getMyShop(HttpSession session){
		Member member = (Member) session.getAttribute("member");
		
		List<Shop> shops = shopService.getShopByMember(memberService.getMemberIdx(member));
		
		return shops;
	}
	
	/*
	    * 필터를 적용한 가게 조회 거리, 가격, 마감시간
	    */
	   @GetMapping("/getShop/filter")
	    public List<Shop> getShopFilterDistance(@RequestParam("latitude") String myLatitude,
	                                          @RequestParam("longitude") String myLongitude,
	                                          @RequestParam(value = "distance", defaultValue = "0") double dist,
	                                          @RequestParam(value = "unit", defaultValue = "km") String unit,
	                                          @RequestParam(value = "maxprice", defaultValue = "0") int maxPrice,
	                                          @RequestParam(value = "minprice", defaultValue = "0") int minPrice,
	                                          @RequestParam(value = "time", defaultValue = "0") String time,
	                                          @RequestParam(value = "rating", defaultValue = "0") double rating){
	      
	      
	       List<Shop> allShops = shopService.getShops();
	      
	       double latitude = Double.parseDouble(myLatitude);
	       double longitude = Double.parseDouble(myLongitude);
	       long minute = Long.parseLong(time) * 60;
	       
	       
	       if(dist != 0) {
	          List<Shop> distanceFilteredShops = shopService.runDistanceFilter(latitude, longitude, dist, unit);
	          if(distanceFilteredShops != null) {   
	             allShops.retainAll(distanceFilteredShops); 
	          }
	       }   
	       if(maxPrice != 0) {
	          List<Shop> priceFilteredShops = shopService.runPriceFilter(maxPrice, minPrice);
	          if(priceFilteredShops != null) {   
	             allShops.retainAll(priceFilteredShops);
	          }
	       }
	       if(minute != 0) {
	           List<Shop> deadlineFilteredShops = shopService.runDeadlineFilter(minute);   
	           if(deadlineFilteredShops != null) {   
	              allShops.retainAll(deadlineFilteredShops); 
	           }
	        }       
	       if(rating != 0) {
	          List<Shop> ratingFilteredShops = shopService.runRatingFilter(rating);
	          if(ratingFilteredShops != null) {
	             allShops.retainAll(ratingFilteredShops);
	          }
	       }
	      
	       return allShops;
	    }
    
    
    /*
     * 별점 추가
     */
    @PostMapping("/setRating")
    public void setRating(HttpSession session,
                          @RequestParam("shopidx") int shopidx,
                          @RequestParam("rating") int rating) {
       
       
       Member member = (Member) session.getAttribute("member");
       int memberidx = member.getMemberIdx(); 
       
       Ratings ratings = new Ratings(0,shopidx,memberidx,rating);
       
       shopService.setRating(ratings);
    }  
    
    /*
     * 해당 아이템 별로 예약자 리스트 조회
     */
    @GetMapping("/shop/item/reservations")
    public List<Map<String, Object>> getItemReservations(@RequestParam("itemidx") int itemidx){
    	log.info("itemidx = {}", itemidx);
    	
    	List<Map<String, Object>> map = shopService.getItemReservations(itemidx);
    	
    	log.info("map.reservationidx={}", map.get(0).get("reservationidx"));
    	
    	return shopService.getItemReservations(itemidx);
    }
    
}