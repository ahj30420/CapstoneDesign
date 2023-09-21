package hello.capstone.repository;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hello.capstone.dto.Shop;

@Mapper
public interface ShopMapper {

   Shop findByAddress(@Param("address") String address);  
   
   void saveShop(Shop shop);
  
}