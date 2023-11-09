package hello.capstone.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Notice {
	private int noticeIdx;
	private String title;
	private String content;
	private Timestamp noticeDate;
	private String noticeModify; //수정됨 or null
	private int memberIdx;
	public Notice() {
		
	}
}