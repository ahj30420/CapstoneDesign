package hello.capstone.dto;

import java.sql.Timestamp;
import java.util.Optional;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import hello.capstone.validation.group.SaveItemValidationGroup;
import hello.capstone.validation.group.UpdateItemValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Item {

	private int itemidx;
	private int shopidx;
	
	@Size(min = 1, max = 20,groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	@NotBlank(groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	private String itemname;
	
	@Range(min = 1, max = 100000, groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	@NotNull(groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	private int cost;
	
	@Range(min = 1, max = 100000,groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	@NotNull(groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	private int salecost;
	
	@Range(min = 1, max = 1000, groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	@NotNull(groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	private int quantity;
	
	private String itemnotice;
	
	@NotNull(groups = SaveItemValidationGroup.class)
	private MultipartFile imageFile;
	
	private String image;
	
	@Size(min = 1, max = 20,groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	@NotBlank(groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	private String category;
	
	private Timestamp  starttime;
	
	private Timestamp  endtime;
	
	public Item() {}
	
}