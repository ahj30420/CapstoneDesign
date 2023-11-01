package hello.capstone.dto;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Item {

	private int itemidx;
	private int shopidx;
	private String itemname;
	private int cost;
	private int salecost;
	private int quantity;
	private String itemnotice;
	private MultipartFile imageFile;
	private String image;
	private String category;
	private Timestamp  starttime;
	private Timestamp  endtime;
	
	public Item() {}
	
}