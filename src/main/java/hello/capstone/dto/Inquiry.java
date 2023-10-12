package hello.capstone.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Inquiry {

	private int inquiryidx;
    private int useridx;
    private Date redate;
    private String content_inquiry;
    private int adminidx;
    private Date answer_redate;
    private String content_answer;
    private String status;

    public Inquiry() {}
    
}
