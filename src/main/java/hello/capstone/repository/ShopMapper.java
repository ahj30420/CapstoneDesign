package hello.capstone.repository;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hello.capstone.dto.Shop;

@Mapper
public interface ShopMapper {

   Shop findByAddress(@Param("address") String address);  
   
   void saveShop(Shop shop);
  
   int getShopIdx(Shop shop);
   
   //shop mark 테스트용
   List<Shop> getShops();
}