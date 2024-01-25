package com.example.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.entity.RestBean;
import com.example.entity.dto.Client;
import com.example.service.ClientService;
import com.example.utils.Const;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 用于对请求头中Jwt令牌进行校验的工具，为当前请求添加用户验证信息
 * 并将用户的ID存放在请求对象属性中，方便后续使用
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    JwtUtils utils;

    @Resource
    ClientService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String uri = request.getRequestURI();
        // 对监控服务的请求进行校验
        if(uri.startsWith("/monitor")) {
            // 如果是注册请求，不需要校验
            if(!uri.endsWith("/register")) {
                Client client = service.findClientByToken(authorization);
                // 如果未注册还请求的不是注册接口，返回401
                if(client == null) {
                    response.setStatus(401);
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(RestBean.failure(401, "未注册").asJsonString());
                    return;
                // 如果已注册，将客户端信息存放在请求属性中
                } else {
                    request.setAttribute(Const.ATTR_CLIENT, client);
                }
            }
        } else { // 对其他服务（网页端）的请求进行校验
            DecodedJWT jwt = utils.resolveJwt(authorization);
            if(jwt != null) {
                UserDetails user = utils.toUser(jwt);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute(Const.ATTR_USER_ID, utils.toId(jwt));
            }
        }
        filterChain.doFilter(request, response);
    }
}
