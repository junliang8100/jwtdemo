package com.offcn.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.offcn.pojo.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * 如果验证用户名和密码正确后。生成token 并且将token返回客户端
 * 集成usernamePasswordauthticationFilter 重写其中的2个方法
 * 第一个方法：接受用户请求 并且解析登录用户的凭证
 * 第二个方法：用户登录成功后。这个方法会被调用。我们需要在当前方法内生成token
 * */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
    //接受并且解析用户凭证
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(),User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    new ArrayList<>()
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //用户登录成功之后生成token
    protected void successfulAuthentication(HttpServletRequest request,HttpServletResponse response,
        FilterChain chain,Authentication auth
    ){
        String token = Jwts.builder()
                .setSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal())
                .getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
                .signWith(SignatureAlgorithm.HS512, "MyJwtSecret")
                .compact();
        response.addHeader("Authorization","Bearer"+token);
    }
}
