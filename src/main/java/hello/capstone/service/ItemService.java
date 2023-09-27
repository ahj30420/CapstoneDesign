package hello.capstone.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hello.capstone.dto.Item;
import hello.capstone.exception.SaveItemException;
import hello.capstone.exception.SaveShopException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemService {

	private final ItemRepository itemRepository;
	
	public boolean itemsave(Item item) {
		
		Optional.ofNullable(itemRepository.findByShopIdx_itemname(item.getShopidx(), item.getItemname()))
			.ifPresent(user->{
				throw new SaveItemException(ErrorCode.DUPLICATED_ITEM,null);
			});
		
		itemRepository.saveitem(item);
		
		return true;
	}
	
}
