package hello.capstone.dto;



import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import hello.capstone.validation.group.SaveShopValidationGroup;
import hello.capstone.validation.group.UpdateShopValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class Shop {
   private int shopidx;
   
   @Size(min = 2, max = 20, groups = {SaveShopValidationGroup.class,UpdateShopValidationGroup.class})   
   @NotBlank(groups = {SaveShopValidationGroup.class,UpdateShopValidationGroup.class})
   private String shopName;
   
   private int ownerIdx;
   
   @NotNull(groups = SaveShopValidationGroup.class)
   private MultipartFile imageFile;
   
   private String imageFilename;
   
   @NotBlank(groups = SaveShopValidationGroup.class)
   private String shopAddress;
   
   @Pattern(regexp = "^\\d{10,11}$", groups = {SaveShopValidationGroup.class,UpdateShopValidationGroup.class})
   @NotBlank(groups = {SaveShopValidationGroup.class,UpdateShopValidationGroup.class})
   private String shopTel;
   
   private String shopWebsite;
   
   private String promotionText;
   
   private Date registrationDate;
   
   private String longitude;
   
   private String latitude;
   
   private double rating;
   
   public Shop() {
      
   }
}