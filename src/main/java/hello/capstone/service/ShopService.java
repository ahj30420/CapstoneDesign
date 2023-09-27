package hello.capstone.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import hello.capstone.dto.Coordinates;
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
	
	@Value("${kakao.local.key}")
	String kakaoLocalKey;
	String uri = "https://dapi.kakao.com/v2/local/search/address.json";
	
	public boolean saveShop(Shop shop) {
		
		log.info("saveShop Start");
		//.ifPresent()는 memberRepository.findById 실행 시 오류 던져주기 위함
		Optional.ofNullable(shopRepository.findByAddress(shop.getShopAddress()))
			.ifPresent(user->{
				throw new SaveShopException(ErrorCode.DUPLICATED_SHOP,null);
			});
		
		long miliseconds = System.currentTimeMillis();
		Date registrationDate = new Date(miliseconds);
		shop.setRegistrationDate(registrationDate);

		log.info("saveShop Middle");
		
		//주소로 경도, 위도 뽑아서 shop에 저장
		String shop_address = shop.getShopAddress();
		Coordinates cor = getCoordinate(shop_address);
		shop.setLongitude(cor.getX());
		shop.setLatitude(cor.getY());
		
		log.info("shop info = {}", shop);
		
		return shopRepository.saveShop(shop);
	}
	
	/*
	 *  주소를 경도, 위도로 반환
	 */
	public Coordinates getCoordinate(String shop_address){
        RestTemplate restTemplate = new RestTemplate();

        String apiKey = "KakaoAK " + kakaoLocalKey;
        String address = shop_address;
        
        // 요청 헤더에 만들기, Authorization 헤더 설정하기
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(uri)
                .queryParam("query",address)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity, String.class);

        // API Response로부터 body 뽑아내기
        String body = response.getBody();
        JSONObject json = new JSONObject(body);
        // body에서 좌표 뽑아내기
        JSONArray documents = json.getJSONArray("documents");
        String x = documents.getJSONObject(0).getString("x");
        String y = documents.getJSONObject(0).getString("y");

        return new Coordinates(x, y);
    }
	
	/*
	 * shop mark 표시 테스트용
	 */
	public List<Shop> getShops(){
		return shopRepository.getShops();
	}
	
	public int getShopIdx(Shop shop) {
		int idx = shopRepository.getShopIdx(shop);
		return idx;
	}
}