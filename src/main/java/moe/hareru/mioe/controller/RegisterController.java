package moe.hareru.mioe.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import moe.hareru.mioe.entity.JSONResponseEntity;
import moe.hareru.mioe.entity.UserEntity;
import moe.hareru.mioe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "User Register")
@RestController
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "用户注册")
    //@ApiOperationSupport(includeParameters = {"password","nickname"})
    @ApiImplicitParams({
    })
    @PostMapping(value = "/register")
    public JSONResponseEntity addNewUser(@RequestBody UserEntity newUser) {
        if (newUser.getPassword()==null)
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Parameter missing", null);
        newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        newUser.setRole("ROLE_USER");//default role
        if (userService.addNewUser(newUser))
            return new JSONResponseEntity(HttpStatus.OK.value(), "OK", newUser);
        else
            return new JSONResponseEntity(HttpStatus.BAD_REQUEST.value(), "Register failure", null);
    }
}
