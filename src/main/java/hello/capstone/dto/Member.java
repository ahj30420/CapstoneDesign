package hello.capstone.dto;

import java.sql.Date;

import hello.capstone.validation.group.SignUpValidationGroup;
import hello.capstone.validation.group.UpdateInfoValidationGroup;
import hello.capstone.validation.group.UpdatePwValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 마지막수정 09/18 14시 20분
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Member {
   
   private int memberIdx;
   
   @Email(groups = SignUpValidationGroup.class)
   @NotBlank(groups = SignUpValidationGroup.class)
   private String id;
    
   @Size(min = 8, max = 16, groups = {SignUpValidationGroup.class,UpdatePwValidationGroup.class})
   @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{7,15}$", groups = SignUpValidationGroup.class)
   @NotBlank(groups = SignUpValidationGroup.class)
   private String pw;
    
   @Pattern(regexp = "^[가-힣a-zA-Z]{1,6}$", groups = {SignUpValidationGroup.class,UpdateInfoValidationGroup.class})
   @NotBlank(groups = SignUpValidationGroup.class)
   private String name;

   @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,15}$", groups = UpdateInfoValidationGroup.class)
   private String nickname;
    
   @Pattern(regexp = "^01[016789]\\d{7,8}$", groups = {SignUpValidationGroup.class, UpdateInfoValidationGroup.class})
   @NotBlank(groups = SignUpValidationGroup.class)
   private String phone;
     
   private String social;
    
   @NotBlank(groups = SignUpValidationGroup.class)
   private String role;
    
   private Date redate;
   
   private String trust;
   
   public void maskSensitiveInformation() {
      this.pw = "********************";
   }

}