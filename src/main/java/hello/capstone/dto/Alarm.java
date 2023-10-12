package hello.capstone.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Alarm {
	private int alarmIdx;
	private int memberIdx;
	private int shopIdx;
	private Timestamp regisdate;
}