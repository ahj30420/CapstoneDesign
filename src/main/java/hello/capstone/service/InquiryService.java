package hello.capstone.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import hello.capstone.dto.Inquiry;
import hello.capstone.exception.InquiryException;
import hello.capstone.exception.errorcode.ErrorCode;
import hello.capstone.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class InquiryService {

   private final InquiryRepository inquiryRepository;
   
   /*
    * 1:1문의 나열 전체
    */
   public List<Map<String, Object>> inquiryViewAll(){
      return inquiryRepository.inquiryViewAll();
   }
   
   /*
    * 1:1문의 나열 한명
    */
   public List<Map<String, Object>> inquiryView(int memberidx){
      return inquiryRepository.inquiryView(memberidx);
   }
   
   /*
    * 1:1 문의 답변 보기(사용자 입장)
    */
   public Map<String, Object> inquiryAnswerView(int inquiryidx) {
      return inquiryRepository.inquiryAnswerView(inquiryidx);
   }
   
   /*
    * 1:1문의 등록
    */
   public void register(Inquiry inquiry) {
      inquiryRepository.register(inquiry);
   }
   
   /*
    * 1:1문의 삭제
    */
   public void delete(int inquiryidx) {
      inquiryRepository.delete(inquiryidx);
   }
   
   /*
    * 1:1문의 수정
    */
   public void update(int inquiryidx, String content_inquiry) {
      
      String status = inquiryRepository.getStatusByInquiryidx(inquiryidx);
      
      if(!status.equals("답변 대기")) {
         throw new InquiryException(ErrorCode.EXIST_ANSWER, null);
      }
      
      long miliseconds = System.currentTimeMillis();
      Date redate = new Date(miliseconds);
      
      inquiryRepository.update(inquiryidx, redate, content_inquiry);      
   }
   
   /*
    * 1:1문의 답변 등록
    */
   public void inquiryAnswer(Inquiry inquiry) {
      inquiryRepository.inquiryAnswer(inquiry);
   }
   
   /*
    * 1:1문의 답변 삭제
    */
   public void inquiryAnswerDelete(int inquiryidx, int adminidx) {
      inquiryRepository.inquiryAnswerDelete(inquiryidx,adminidx);
   }
   
   /*
    * 1:1문의 답변 수정
    */
   public void inquiryAnswerUpdate(int inquiryidx, int adminidx, String content_answer) {
      
      long miliseconds = System.currentTimeMillis();
      Date answer_redate = new Date(miliseconds);
      
      inquiryRepository.inquiryAnswerUpdate(inquiryidx, answer_redate, adminidx, content_answer);   
      
   }
   
}