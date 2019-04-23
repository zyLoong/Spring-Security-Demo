package com.zyloong.springsecurity.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyloong.springsecurity.demo.entity.AuthenticationBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 登录认证过滤器
 *
 * @author zyloong
 */
public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /**
     * 登录认证，如果是JSON登录，则使用自定义的认证逻辑
     * 如果是表单登陆，则使用父类的认证逻辑。
     * 本项目登录方法使用JSON参数，而非表单参数
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String contentType = request.getContentType();
        String jsonUtf8Value = MediaType.APPLICATION_JSON_UTF8_VALUE;
        String jsonValue = MediaType.APPLICATION_JSON_VALUE;
        //attempt Authentication when Content-Type is json
        if (contentType.equalsIgnoreCase(jsonUtf8Value) || contentType.equalsIgnoreCase(jsonValue)) {

            //use jackson to deserialize jsongetMashineReadFilesAndNormalize
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest;
            try (InputStream is = request.getInputStream()) {
                AuthenticationBean authenticationBean = mapper.readValue(is, AuthenticationBean.class);
                authRequest = new UsernamePasswordAuthenticationToken(authenticationBean.getUsername(), authenticationBean.getPassword());
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        //transmit it to UsernamePasswordAuthenticationFilter
        return super.attemptAuthentication(request, response);
    }
}
