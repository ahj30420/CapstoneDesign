package hello.capstone.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import hello.capstone.dto.Coordinates;
import hello.capstone.dto.Item;
import hello.capstone.dto.Ratings;
import hello.capstone.dto.Shop;
import hello.capstone.exception.SaveShopException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.ItemRepository;
import hello.capstone.repository.RatingsRepository;
import hello.capstone.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShopService {
	
	private final ShopRepository shopRepository;
	private final ItemRepository itemRepository;
	private final RatingsRepository ratingsRepository;
	
	@Value("${kakao.local.key}")
	String kakaoLocalKey;
	String uri = "https://dapi.kakao.com/v2/local/search/address.json";
	
	@Value("${file.dir}")
	private String fileDir;
	
	/*
	 * 가게등록
	 */
	public void saveShop(Shop shop) throws IllegalStateException, IOException {
		//해당 주소에 가게가 이미 존재하는지 판단
		Optional.ofNullable(shopRepository.findByAddress(shop.getShopAddress()))
			.ifPresent(user->{
				throw new SaveShopException(ErrorCode.DUPLICATED_SHOP,null);
			});

		//파일 이름 추출
		MultipartFile imageFile = shop.getImageFile();
		
		if(imageFile != null) {
			String fullPath = fileDir + imageFile.getOriginalFilename();
			log.info("파일 저장 fullPath ={}",fullPath);
			imageFile.transferTo(new File(fullPath));
			shop.setImageFilename(imageFile.getOriginalFilename());
		}
		
		//주소로 경도, 위도 뽑아서 shop에 저장
		String shopAddress = shop.getShopAddress();
		Coordinates cor = getCoordinate(shopAddress);
		shop.setLongitude(cor.getX());
		shop.setLatitude(cor.getY());
		
		
		shopRepository.saveShop(shop);
	}
		
	
	/*
    * 가게 수정
    */
   public void updateShop(Shop shop, MultipartFile image, String address) throws IllegalStateException, IOException {
      
      Shop oldShop = shopRepository.getShopByIdx(shop.getShopidx());
      oldShop.setShopName(shop.getShopName());
      oldShop.setShopTel(shop.getShopTel());
      oldShop.setPromotionText(shop.getPromotionText());
      oldShop.setShopWebsite(shop.getShopWebsite());
      
      //이미지 파일이 새로 바뀐 경우
      if(image != null) {
         String fullPath = fileDir + image.getOriginalFilename();
         log.info("파일 저장 fullPath ={}",fullPath);
         image.transferTo(new File(fullPath));
         oldShop.setImageFilename(image.getOriginalFilename());
      }
      
      //주소가 새로 바뀐 경우 / 위도, 경도까지 새로 적용
      if(!(address.equals("")) ) {
         oldShop.setShopAddress(address);
         
         Coordinates cor = getCoordinate(address);
         oldShop.setLongitude(cor.getX());
         oldShop.setLatitude(cor.getY());
      }
      
      shopRepository.updateShop(oldShop);
   }
	
	public void shopDelete(int shopidx) {
		shopRepository.shopDelete(shopidx);
	}
	
	/*
    * 가격 필터가 적용된 가게 조회
    */
   public List<Shop> runPriceFilter(int maxPrice, int minPrice){
       List<Shop> filteredShops = shopRepository.runPriceFilter(maxPrice, minPrice);
       
       return filteredShops;
   }
	/*
	 * 거리필터가 적용된 가게 조회, 좌표 간 계산식 참고 출처 https://frontmaster.tistory.com/135
	 */
	public List<Shop> runDistanceFilter(double latitude, double longitude, double distance, String unit){
		
		List<Shop> shops = shopRepository.getShops();
		List<Shop> filteredShops = new ArrayList<Shop>();
		
		for (Shop shop : shops) {
			double shopLatitude = Double.parseDouble(shop.getLatitude());
			double shopLongitude = Double.parseDouble(shop.getLongitude());
			
			double theta = longitude - shopLongitude;
	        double dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(shopLatitude)) + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(shopLatitude)) * Math.cos(deg2rad(theta));
	         
	        dist = Math.acos(dist);
	        dist = rad2deg(dist);
	        dist = dist * 60 * 1.1515;
	        
	        if (unit.equals("km")) {
	            dist = dist * 1.609344;
	        } else if(unit.equals("m")){
	            dist = dist * 1609.344;
	        }
	        if(dist <= distance) {
	        	filteredShops.add(shop);
	        }
		}
		
		return filteredShops;
	}
	
	/*
	 * 마감시간 필터가 적용된 가게 조회
	 */
	public List<Shop> runDeadlineFilter(long minute){
		
		List<Shop> shops = shopRepository.getShops();
		List<Shop> filteredShops = new ArrayList<Shop>();
		
		for (Shop shop : shops) {
			List<Item> items = itemRepository.getItems(shop.getShopidx());
			for(Item item : items) {
				LocalDateTime now = LocalDateTime.now();
				Timestamp timestamp = Timestamp.valueOf(now);
				long remainTime = (item.getEndtime().getTime() - timestamp.getTime()) /1000 /60; //분으로 계산
				
				if(remainTime > minute) {
					filteredShops.add(shop);
					break;
				}
			}
		}
		
		return filteredShops;
	}
	
	/*
	 * decimal degrees -> radian
	 * radian -> decimal degrees
	 * 변환
	 */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
     
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
	
	/*
	 *  주소를 경도, 위도로 반환
	 */
	private Coordinates getCoordinate(String shop_address){
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
	 * shop mark 표시 (아이템 등록된 가게만 가져오기)
	 */
	public List<Shop> getShops(){
		
		
		return shopRepository.getShops();
	}
	
	public int getShopIdx(Shop shop) {
		int idx = shopRepository.getShopIdx(shop);
		return idx;
	}
	
	public Shop getShopByIdx(int shopidx) {
		return shopRepository.getShopByIdx(shopidx);
	}
	
	
	/*
	 * 상업자 버전
	 */
	public List<Shop> getShopByMember(int memberidx){
		return shopRepository.getShopByMember(memberidx);
		
	}
	
	 /*
     * 별점 필터가 적용된 가게 조회
     */
    public List<Shop> runRatingFilter(double rating){
   	 List<Shop> filteredShops = shopRepository.runRatingFilter(rating);
   	 return filteredShops;
    }
    
    /*
     * 별점 추가하기
     */
    public boolean setRating(Ratings ratings) {
       
       int shopidx = ratings.getShopidx();
       int memberidx = ratings.getMemberidx();
       
       if(ratingsRepository.existingRatings(shopidx, memberidx) == true) {
          ratingsRepository.updateRatings(ratings);
       }
       else {
          ratingsRepository.setRatings(ratings);
       }
       
       Map ratings_info = ratingsRepository.getSumCount(shopidx);        
       
       log.info("ratings_info = {}", ratings_info);
       log.info("ratings_info.get(\"sum\") = {}", ratings_info.get("sum").getClass());
       
       Double sum = (Double)ratings_info.get("sum");
       log.info("sum = {}", sum);
       Double count = ((Long)ratings_info.get("count")).doubleValue();
       log.info("count = {}", count);
       
       
       double tol_rating = sum / count;
       
       shopRepository.setRatings(shopidx,tol_rating);
       return true;
    }
    
    /*
     * 해당 아이템 별로 예약자 리스트 조회
     */
    public List<Map<String, Object>> getItemReservations(int itemidx){
    	return shopRepository.getItemReservations(itemidx);
    }    
}