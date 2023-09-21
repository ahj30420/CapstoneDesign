package hello.capstone.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hello.capstone.dto.Member;
import hello.capstone.dto.Shop;
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
	public String shopRegistration(@RequestParam("imageFilename") MultipartFile Image,
			  					   @RequestParam("shopName") String shopName,
			  					   @RequestParam("shopTel") String shopTel,
			  					   @RequestParam("shopAddress") String shopAddress,
			  					   @RequestParam("promotionText") String promotionText,
			  					   @RequestParam("shopWebsite") String shopWebsite,
			  					   HttpSession session) throws IllegalStateException, IOException {
		
		//Member ownerMember = (Member)session.getAttribute("member");
		//int ownerIdx = memberService.getMeberIdx(ownerMember);
		Shop shop = new Shop();
		shop.setShopName(shopName);
		shop.setShopTel(shopTel);
		shop.setPromotionText(promotionText);
		shop.setShopAddress(shopAddress);
		shop.setShopWebsite(shopWebsite);
		//shop.setOwnerIdx(ownerIdx);
		
		if(!Image.isEmpty()) {
			String fullPath = fileDir + Image.getOriginalFilename();
			log.info("파일 저장 fullPath ={}",fullPath);
			Image.transferTo(new File(fullPath));
		}
		shop.setImageFilename(Image.getOriginalFilename());
		log.info("가게 파라미터={}",shop);
		log.info("파일 파라미터={}",Image);
		shopService.saveShop(shop);
		
		
		return "/home_user";
	}
}