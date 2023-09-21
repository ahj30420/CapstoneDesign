package hello.capstone.service;

import java.sql.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hello.capstone.dto.Shop;
import hello.capstone.exception.SaveShopException;
import hello.capstone.exception.SignUpException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShopService {
	
	private final ShopRepository shopRepository;
	
	public boolean saveShop(Shop shop) {
		
		//.ifPresent()는 memberRepository.findById 실행 시 오류 던져주기 위함
		Optional.ofNullable(shopRepository.findByAddress(shop.getShopAddress()))
			.ifPresent(user->{
				throw new SaveShopException(ErrorCode.DUPLICATED_SHOP,null);
			});
		
		long miliseconds = System.currentTimeMillis();
		Date registrationDate = new Date(miliseconds);
		shop.setRegistrationDate(registrationDate);
		shop.setOwnerIdx(1);
		shop.setShopAddress("a");
		
		log.info("ShopService - shop = {}", shop);
		
		return shopRepository.saveShop(shop);
	}
}