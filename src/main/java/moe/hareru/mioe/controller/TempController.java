package moe.hareru.mioe.controller;

import moe.hareru.mioe.entity.JSONResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {
    // 任何人都可以访问，在方法中判断用户是否合法
    @GetMapping("everyone")
    public JSONResponseEntity everyone() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            // 登入用户
            return new JSONResponseEntity(HttpStatus.OK.value(), "You are already login", authentication.getPrincipal());
        } else {
            return new JSONResponseEntity(HttpStatus.OK.value(), "You are anonymous", null);
        }
    }

    @GetMapping("user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public JSONResponseEntity user(@AuthenticationPrincipal UsernamePasswordAuthenticationToken token) {
        return new JSONResponseEntity(HttpStatus.OK.value(), "You are user", token);
    }

    @GetMapping("admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public JSONResponseEntity admin(@AuthenticationPrincipal UsernamePasswordAuthenticationToken token) {
        return new JSONResponseEntity(HttpStatus.OK.value(), "You are admin", token);
    }
}
