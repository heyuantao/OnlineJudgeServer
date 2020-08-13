package cn.heyuantao.onlinejudgeserver.filter;

import cn.heyuantao.onlinejudgeserver.auth.AuthService;
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

import javax.security.auth.message.AuthException;
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
    AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String userToken = null;

        try{
            if( authorizationHeader !=null ){
                userToken = authorizationHeader;
            }

            //如果HTTP header中没有Token信息，同时SecurityContextHolder也没有用户的信息，则准备进行认证
            if( userToken!=null && SecurityContextHolder.getContext().getAuthentication() == null ){
                //根据Token来获得用户，如果Token不对，则返回值为空
                SysUser sysUser = authService.loadSysUserByToken(userToken);

                if(sysUser!=null){
                    SysUserDetails sysUserDetails = new SysUserDetails(sysUser);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(sysUserDetails, null, sysUserDetails.getAuthorities());
                    //将登录的用户信息与Request进行绑定,后续可以直接通过SecurityContextHolder来获取用户身份
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }else{
                    throw new AuthException("未找到对应的用户,Token错误！");
                }
            }
        }catch (AuthException ex){
            ErrorDetails errorDetails = new ErrorDetails("认证错误",ex.getMessage());
            responseWithErrorDetails(httpServletResponse,errorDetails,HttpStatus.UNAUTHORIZED);
            return;
        }catch (Exception ex){
            ErrorDetails errorDetails = new ErrorDetails("未知错误","登录出现未知错误");
            responseWithErrorDetails(httpServletResponse,errorDetails,HttpStatus.UNAUTHORIZED);
            return;
        }

        //进行后续的过滤
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
