package hello.capstone.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlarmWithBefore {
	private Object object;
	private int before;
	
	public AlarmWithBefore(){
		
	}
}