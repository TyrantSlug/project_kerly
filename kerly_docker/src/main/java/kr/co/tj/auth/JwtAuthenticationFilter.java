package kr.co.tj.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import kr.co.tj.sec.TokenProvider;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

   @Autowired
   private TokenProvider tokenProvider;

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
         throws ServletException, IOException {

      try {
         String token = parseBearerToken(request);
         //  HTTP 헤더에서 Authorization 헤더 값을 추출,
         //  값이 "Bearer "로 시작하는 JWT 토큰을 추출
         if (token != null && !token.equalsIgnoreCase("null")) {

            String userId = tokenProvider.validateAndGetUserId(token);
            // 추출한 토큰을 TokenProvider를 사용하여 검증,
            // 유저 ID를 가져옴
            String authority = tokenProvider.validateAndAuthority(token);
            // 토큰을 검증하고, 해당 유저의 권한 정보를 가져옴
            AbstractAuthenticationToken aat = new UsernamePasswordAuthenticationToken(userId, null,
                  AuthorityUtils.createAuthorityList(authority));
            // Spring Security에서 인증을 나타내는 토큰
            // 추출한 유저 ID와 권한 정보를 사용하여 이 토큰을 생성

            aat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContext secCtx = SecurityContextHolder.createEmptyContext();
            secCtx.setAuthentication(aat);
            SecurityContextHolder.setContext(secCtx);
         }   //  클라이언트의 요청마다 사용자의 신원과 권한을 확인

      } catch (Exception e) {
         e.printStackTrace();
         System.out.println("인증실패");
      }

      filterChain.doFilter(request, response);
   }

   private String parseBearerToken(HttpServletRequest request) {
      String bearerToken = request.getHeader("Authorization");
      if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
         return null;
      }

      return bearerToken.substring(7);
   }

}