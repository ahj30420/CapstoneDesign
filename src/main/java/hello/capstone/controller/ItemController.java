package hello.capstone.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hello.capstone.dto.Item;
import hello.capstone.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/item")
@RestController
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;
	
	@Value("${file.dir}")
	private String fileDir;
	
	/*
	 * 아이템 등록
	 */
	@PostMapping("/register")
	public String ItemRegistration(@RequestParam("image") MultipartFile Image,
								   @RequestParam("shopidx") int shopidx,
								   @RequestParam("itemname") String itemname,
								   @RequestParam("cost") int cost,
								   @RequestParam("salecost") int salecost,
								   @RequestParam("quantity") int quantity,
								   @RequestParam("category") String category,
								   @RequestParam("starttime") Date starttime,
								   @RequestParam("endtime") Date endtime) throws IllegalStateException, IOException {
		
		Item item = new Item();
		item.setShopidx(shopidx);
		item.setItemname(itemname);
		item.setCost(cost);
		item.setSalecost(salecost);
		item.setQuantity(quantity);
		item.setCategory(category);
		item.setStarttime(starttime);
		item.setEndtime(endtime);
		
		if(!Image.isEmpty()) {
			String fullPath = fileDir + Image.getOriginalFilename();
			Image.transferTo(new File(fullPath));
		}
		
		item.setImage(Image.getOriginalFilename());
		
		itemService.itemsave(item);
		return "success";
	}
	
}
