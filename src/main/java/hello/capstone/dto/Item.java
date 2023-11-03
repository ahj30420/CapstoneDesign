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
	
	@Size(min = 1, max = 20, message = "상품 이름은 20자 이내여야 합니다.", groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	@NotBlank(message = "상품 이름은 필수 입력항목입니다.(공백 사용X)", groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	private String itemname;
	
	@Range(min = 1, max = 100000, message = "원가는 1~1000000원 사이여야 합니다.", groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	@NotNull(message = "원가는 필수 입력항목입니다.(공백 사용X)", groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	private Integer cost;
	
	@Range(min = 1, max = 100000, message = "할인가격은 1~1000000원 사이여야 합니다.", groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	@NotNull(message = "할인가격은 필수 입력항목입니다.(공백 사용X)", groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	private Integer salecost;
	
	@Range(min = 1, max = 1000, message = "수량은 1~1000개 사이여야 합니다.", groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	@NotNull(message = "수량은 필수 입력항목입니다.(공백 사용X)", groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	private Integer quantity;
	
	private String itemnotice;
	
	private MultipartFile imageFile;
	
	private String image;
	
	@Size(min = 1, max = 20, message = "상품 이름은 20자 이내여야 합니다.", groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	@NotBlank(message = "카테고리는 필수 입력항목입니다.(공백 사용X)", groups = {SaveItemValidationGroup.class, UpdateItemValidationGroup.class})
	private String category;
	
	private Timestamp  starttime;
	
	private Timestamp  endtime;
	
	public Item() {}
	
}