package hello.capstone.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hello.capstone.dto.Member;
import hello.capstone.dto.Shop;
import hello.capstone.exception.LogInException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.service.MemberService;
import hello.capstone.service.ShopService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ShopController {
	
	private final MemberService memberService;
	private final ShopService shopService;
	
	@Value("${file.dir}")
	private String fileDir;
	
	@PostMapping("/shopRegistration")
	public String shopRegistration(@RequestParam(value = "imageFilename", required = false) MultipartFile Image,
								   @RequestParam(value = "shopidx", required = false) String sidx,
			  					   @RequestParam("shopName") String shopName,
			  					   @RequestParam("shopTel") String shopTel,
			  					   @RequestParam("shopAddress") String shopAddress,
			  					   @RequestParam("promotionText") String promotionText,
			  					   @RequestParam("shopWebsite") String shopWebsite,
			  					   @RequestParam(value = "existingImage", required = false) String existingImage,
			  					   @RequestParam(value = "method", defaultValue = "register") String method,
			  					   HttpSession session) throws IllegalStateException, IOException {
		
		Member ownerMember = (Member)session.getAttribute("member");
		int memberidx = memberService.getMeberIdx(ownerMember);
		
		Shop shop = new Shop();
		shop.setShopName(shopName);
		shop.setShopTel(shopTel);
		shop.setPromotionText(promotionText);
		shop.setShopAddress(shopAddress);
		shop.setShopWebsite(shopWebsite);
		shop.setOwnerIdx(memberidx);
		
		if(!Image.isEmpty()) {
			String fullPath = fileDir + Image.getOriginalFilename();
			log.info("파일 저장 fullPath ={}",fullPath);
			Image.transferTo(new File(fullPath));
			shop.setImageFilename(Image.getOriginalFilename());
		}
		else {
			int shopidx = Integer.parseInt(sidx);
			shop.setShopidx(shopidx);
			shop.setImageFilename(existingImage);
		}
		log.info("가게 파라미터={}",shop);
		log.info("파일 파라미터={}",Image);
		
		shopService.saveShop(shop, method);
		
		return "/home_user";
	}
	
	/*
	 * 지도 shop marker 표시 테스트용 (모든 shop)
	 */
	@GetMapping("/ShopMarker")
	public List<Shop> ShopAdress(HttpSession session){
		List<Shop> shops = shopService.getShops();
		
		return shops;
	} 
	
	/*
	 * 본인의 가게 정보 가져오기 (상업자 버전)
	 */
	@GetMapping("/getMyShop")
	public List<Shop> getMyShop(HttpSession session){
		Member member = (Member) session.getAttribute("member");
		
		List<Shop> shops = shopService.getShopByMember(memberService.getMeberIdx(member));
		
		log.info("shops = {} ", shops);
		return shops;
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
     * 필터 적용 가게 조회
     */
    @GetMapping("/getShop/filter")
    public List<Shop> getShopFilterDistance(@RequestParam("latitude") String myLatitude,
                                  @RequestParam("longitude") String myLongitude,
                                  @RequestParam(value = "distance", defaultValue = "0") String distance,
                                  @RequestParam(value = "unit", defaultValue = "m") String unit,
                                  @RequestParam(value = "price", defaultValue = "0") String itemprice){
      

       List<Shop> allShops = shopService.getShops();
      
       log.info("allShops = {}", allShops);
       double latitude = Double.parseDouble(myLatitude);
       double longitude = Double.parseDouble(myLongitude);
       double dist = Double.parseDouble(distance);
       int price = Integer.parseInt(itemprice);
       
       if(dist != 0) {
          List<Shop> distanceFilteredShops = shopService.runDistanceFilter(latitude, longitude, dist, unit);
          if(distanceFilteredShops != null) {   
             allShops.retainAll(distanceFilteredShops);
             log.info("distanceFilteredShops = {}", distanceFilteredShops);   
          }
       }   
       if(price != 0) {
          List<Shop> priceFilteredShops = shopService.runPriceFilter(price);
          if(priceFilteredShops != null) {   
             allShops.retainAll(priceFilteredShops);
             log.info("priceFilteredShops= {}", priceFilteredShops);   
          }
       }
       
      
       return allShops;
    }

}