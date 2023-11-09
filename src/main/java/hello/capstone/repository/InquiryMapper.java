package hello.capstone.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hello.capstone.dto.Inquiry;

@Mapper
public interface InquiryMapper {

   List<Map<String, Object>> inquiryViewAll();
   
   List<Map<String, Object>> inquiryView(int memberidx);
   
   Map<String, Object> inquiryAnswerView(int inquiryidx);
   
   void register(Inquiry inquiry);
   
   void delete(int inquiryidx);
   
   void update(@Param("inquiryidx") int inquiryidx, @Param("redate") Date redate, @Param("content_inquiry") String content_inquiry);
   
   String getStatusByInquiryidx(int inquiryidx);
   
   void inquiryAnswer(Inquiry inquiry);
   
   void inquiryAnswerDelete(@Param("inquiryidx") int inquiryidx, @Param("adminidx") int adminidx);
   
   void inquiryAnswerUpdate(@Param("inquiryidx") int inquiryidx, @Param("answer_redate") Date answer_redate, @Param("adminidx") int adminidx, @Param("content_answer") String content_answer);
}