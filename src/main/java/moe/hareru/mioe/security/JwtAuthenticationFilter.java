package moe.hareru.mioe.security;

import com.alibaba.fastjson.JSONObject;
import moe.hareru.mioe.entity.JSONResponseEntity;
import moe.hareru.mioe.util.JSONWebTokenUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super();//Set @PostMapping("/login") by super constructor.
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userIdStr = request.getParameter("userId");
        String password = request.getParameter("password");
        if (Objects.equals(request.getHeader("Content-Type"), "application/json")) {
            try {
                JSONObject reqBody = JSONObject.parseObject(IOUtils.toString(request.getReader()));
                userIdStr = reqBody.getString("userId");
                password = reqBody.getString("password");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userIdStr, password);
        setDetails(request, token);
        return getAuthenticationManager().authenticate(token); // Pass to AuthenticationManager for authentication
    }

    @Override // success
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        JSONResponseEntity jsonResponseEntity = new JSONResponseEntity();
        response.setHeader("Content-Type", "application/json;charset=UTF-8");

        User user = (User) authResult.getPrincipal();
        String expireStr = request.getParameter("expire");
        Long expireMs = expireStr == null ? null : Long.parseLong(expireStr);
        String token = JSONWebTokenUtil.sign(Long.parseLong(user.getUsername()), user.getPassword(), expireMs);

        jsonResponseEntity.setStatus(HttpStatus.OK.value());
        jsonResponseEntity.setMsg("OK");
        jsonResponseEntity.setData("Bearer " + token);

        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(jsonResponseEntity.toString());
    }

    @Override // fail
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        JSONResponseEntity jsonResponseEntity = new JSONResponseEntity();
        response.setHeader("Content-Type", "application/json;charset=UTF-8");

        jsonResponseEntity.setStatus(HttpStatus.BAD_REQUEST.value());
        jsonResponseEntity.setMsg("Wrong userId or password");
        jsonResponseEntity.setData(null);

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getWriter().write(jsonResponseEntity.toString());
    }

}
