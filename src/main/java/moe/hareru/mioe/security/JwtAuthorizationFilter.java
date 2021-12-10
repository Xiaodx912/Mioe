package moe.hareru.mioe.security;

import moe.hareru.mioe.util.JSONWebTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserDetailsService userDetailsService;

    // Passed from Spring Security configure
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication token = getAuthentication(request);
        if (token != null) {// Authorize success
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        chain.doFilter(request, response); // pass to filter chain
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.split(" ")[1];
        Long userId = JSONWebTokenUtil.getUserId(token);
        String userIdStr = (userId == null) ? null : userId.toString();
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(userIdStr);
        } catch (UsernameNotFoundException e) {
            return null;
        }
        if (!JSONWebTokenUtil.verify(token, userId, userDetails.getPassword())) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
