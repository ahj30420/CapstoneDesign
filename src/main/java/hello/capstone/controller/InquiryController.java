package hello.capstone.controller;

import java.sql.Date;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.capstone.dto.Inquiry;
import hello.capstone.service.InquiryService;
import hello.capstone.service.ItemService;
import hello.capstone.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/inquiry")
@RestController
@RequiredArgsConstructor
public class InquiryController {

	private final InquiryService inquiryService;
	
	/*
	 * 1:1문의 등록
	 */
	@PostMapping("/register")
	public String inquiryRegister(@RequestParam("useridx") String uidx,
								  @RequestParam("content_inquiry") String content_inquiry) {
		
		Inquiry inquiry = new Inquiry();
		
		int useridx = Integer.parseInt(uidx);
		long miliseconds = System.currentTimeMillis();
		Date redate = new Date(miliseconds);
		
		inquiry.setUseridx(useridx);
		inquiry.setContent_inquiry(content_inquiry);
		inquiry.setRedate(redate);
		
		inquiryService.register(inquiry);
		
		return "";
	}
	
	/*
	 * 1:1문의 삭제
	 */
	@DeleteMapping("/delete")
	public void inquiryDelete(@RequestParam("inquiryidx") String iqidx) {
		
		int inquiryidx = Integer.parseInt(iqidx);
		
		inquiryService.delete(inquiryidx);
	}
	
	/*
	 * 1:1문의 수정
	 */
	@PutMapping("/update")
	public String inquiryUpdate(@RequestParam("inquiryidx") String iqidx,
								@RequestParam("content_inquiry") String content_inquiry) {
		
		int inquiryidx = Integer.parseInt(iqidx);
		
		inquiryService.update(inquiryidx, content_inquiry);
		
		return "";
	}
	
	/*
	 * 1:1문의 답변 등록
	 */
	@PostMapping("/answer")
	public String inquiryAnswer(@RequestParam("inquiryidx") String iqidx,
			 					@RequestParam("adminidx") String adidx,
			 					@RequestParam("content_answer") String content_answer) {
		
		int inquiryidx = Integer.parseInt(iqidx);
		int adminidx = Integer.parseInt(adidx);
		long miliseconds = System.currentTimeMillis();
		Date answer_redate = new Date(miliseconds);
		
		Inquiry inquiry = new Inquiry(inquiryidx, 0, null, null, adminidx, answer_redate, content_answer, "답변 완료");
		
		inquiryService.inquiryAnswer(inquiry);
		
		return "";
	}
	
	/*
	 * 1:1문의 답변 삭제
	 */
	@DeleteMapping("/answer/delete")
	public void inquiryAnswerDelete(@RequestParam("inquiryidx") String iqidx,
									@RequestParam("adminidx") String adidx) {
		
		int inquiryidx = Integer.parseInt(iqidx);
		int adminidx = Integer.parseInt(adidx);
		
		inquiryService.inquiryAnswerDelete(inquiryidx,adminidx);
	}
	
	/*
	 * 1:1문의 답변 수정
	 */
	@PutMapping("/answer/update")
	public String inquiryAnswerUpdate(@RequestParam("inquiryidx") String iqidx,
									  @RequestParam("adminidx") String adidx,
									  @RequestParam("content_answer") String content_answer) {
		
		int inquiryidx = Integer.parseInt(iqidx);
		int adminidx = Integer.parseInt(adidx);
		
		inquiryService.inquiryAnswerUpdate(inquiryidx, adminidx, content_answer);
		
		return "";
	}
	
}
