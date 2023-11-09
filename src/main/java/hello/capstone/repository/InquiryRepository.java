package hello.capstone.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import hello.capstone.dto.Inquiry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class InquiryRepository {

   private final InquiryMapper inquiryMapper;
   
   /*
     * 1:1문의 나열 전체
     */
    public List<Map<String, Object>> inquiryViewAll(){
       return inquiryMapper.inquiryViewAll();
    }
    
    /*
     * 1:1문의 나열 한명
     */
    public List<Map<String, Object>> inquiryView(int memberidx){
       return inquiryMapper.inquiryView(memberidx);
    }
   
   /*
    * 1:1 문의 답변 보기(사용자 입장)
    */
   public Map<String, Object> inquiryAnswerView(int inquiryidx) {
      return inquiryMapper.inquiryAnswerView(inquiryidx);
   }
   
   /*
    * 1:1문의 등록
    */
   public void register(Inquiry inquiry) {
      inquiryMapper.register(inquiry);
   }
   
   /*
    * 1:1문의 삭제
    */
   public void delete(int inquiryidx) {
      inquiryMapper.delete(inquiryidx);
   }
   
   /*
    * 1:1문의 수정
    */
   public void update(int inquiryidx, Date redate, String content_inquiry) {
      inquiryMapper.update(inquiryidx, redate, content_inquiry);
   }
   
   /*
    * 특정 문의에 대한 답변 형황 조회
    */
   public String getStatusByInquiryidx(int inquiryidx) {
      return inquiryMapper.getStatusByInquiryidx(inquiryidx);
   }
   
   /*
    * 1:1문의 답변 등록
    */
   public void inquiryAnswer(Inquiry inquiry) {
      inquiryMapper.inquiryAnswer(inquiry);
   }
   
   /*
    * 1:1문의 답변 삭제
    */
   public void inquiryAnswerDelete(int inquiryidx, int adminidx) {
      inquiryMapper.inquiryAnswerDelete(inquiryidx, adminidx);
   }
   
   /*
    * 1:1문의 답변 수정
    */
   public void inquiryAnswerUpdate(int inquiryidx, Date answer_redate, int adminidx, String content_answer) {
      inquiryMapper.inquiryAnswerUpdate(inquiryidx, answer_redate, adminidx, content_answer);
   }
}