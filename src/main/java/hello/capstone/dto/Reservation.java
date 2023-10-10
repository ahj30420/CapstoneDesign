package hello.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reservation {

	private int reservationidx;
	private int memberidx;
	private int shopidx;
	private int itemidx;
	private int number;
	
}
