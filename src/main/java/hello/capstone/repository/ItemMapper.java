package hello.capstone.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hello.capstone.dto.Item;

@Mapper
public interface ItemMapper {

	void saveitem(Item item);
	
	Item findByShopIdx_itemname(@Param("shopidx") int shopidx, @Param("itemname") String itemname);
}
