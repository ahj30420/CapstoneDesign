package hello.capstone.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Item {

	private int shopidx;
	private String itemname;
	private int cost;
	private int salecost;
	private int quantity;
	private String image;
	private String category;
	private Date starttime;
	private Date endtime;
	
	public Item() {}
	
}
