package hello.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ratings {

	private int ratingsidx;
	private int shopidx;
	private int memberidx;
	private int rating;
	
}
