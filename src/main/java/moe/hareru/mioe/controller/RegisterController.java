package moe.hareru.mioe.controller;

import com.alibaba.fastjson.JSONObject;
import moe.hareru.mioe.entity.JSONResponseEntity;
import moe.hareru.mioe.entity.UserEntity;
import moe.hareru.mioe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public JSONResponseEntity addNewUser(@RequestBody JSONObject data) {
        if (!data.containsKey("password"))
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Parameter missing", null);
        data.put("password", new BCryptPasswordEncoder().encode(data.getString("password")));
        UserEntity userEntity = JSONObject.toJavaObject(data, UserEntity.class);
        userEntity.setRole("ROLE_USER");//Default role
        if (userService.addNewUser(userEntity))
            return new JSONResponseEntity(HttpStatus.OK.value(), "OK", userEntity);
        else
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Register failure", null);
    }
}
