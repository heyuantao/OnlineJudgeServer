package cn.heyuantao.onlinejudgeserver.filter;

import cn.heyuantao.onlinejudgeserver.auth.SysUser;
import cn.heyuantao.onlinejudgeserver.auth.SysUserDetails;
import cn.heyuantao.onlinejudgeserver.exception.ErrorDetails;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author he_yu
 * 基于Token的认证，Token是固定值
 */
@Component
public class TokenAuthFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String userToken = null;
        ErrorDetails errorDetails = null;

        try{
            if( authorizationHeader !=null ){
                userToken = authorizationHeader;

            }

            if( userToken!=null && SecurityContextHolder.getContext().getAuthentication() == null ){
                if(userToken.equals("abc123")){
                    SysUser sysUser = new SysUser("abc123","abc123");
                    SysUserDetails sysUserDetails = new SysUserDetails(sysUser);

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            sysUserDetails, null, sysUserDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }catch (Exception ex){
            errorDetails = new ErrorDetails("未知错误","登录出现未知错误");
            responseWithErrorDetails(httpServletResponse,errorDetails,HttpStatus.UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void responseWithErrorDetails(HttpServletResponse httpServletResponse, ErrorDetails errorDetails, HttpStatus httpStatus) throws IOException {
        String content = null;

        if (errorDetails == null) {
            content = objectMapper.writeValueAsString(new HashMap<>());
        }else{
            content = objectMapper.writeValueAsString(errorDetails);
        }

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(httpStatus.value());
        httpServletResponse.getWriter().write(content);
    }
}
