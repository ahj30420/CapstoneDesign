package hello.capstone.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	/*
	 * 아이템 등록
	 */
	@PostMapping("/register")
	public String ItemRegistration(@ModelAttribute Item item) {
		itemService.itemsave(item);
		return "success";
	}
	
}
