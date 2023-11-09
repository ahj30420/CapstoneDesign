package hello.capstone.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import hello.capstone.dto.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManagerCheckInterceptor implements HandlerInterceptor{

   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      log.info("관리자 인증 인터셉터 실행 {}");
      
      HttpSession session = request.getSession(false);
      
      if(((Member)session.getAttribute("AdminMember") == null || !(((Member)session.getAttribute("AdminMember")).getRole().equals("관리자")))) {
         log.info("미인증 사용자 요청");
         
         response.getWriter().write("{\"redirect\": \"/home_user\"}");
         return false;
      }
      
      return true;
   }
   

   
   
}