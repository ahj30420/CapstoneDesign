package hello.capstone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

	  /*
	   * PasswordEncoder를 Bean으로 등록
	   */
	  @Bean
	  public BCryptPasswordEncoder bCryptPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	  }
	  
	  @Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		  http
		  		.csrf().disable();
		  return http.build();
	  }
 
}
