package moe.hareru.mioe.controller;

import moe.hareru.mioe.entity.JSONResponseEntity;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public JSONResponseEntity handleError(HttpServletRequest request, HttpServletResponse response) {
        return new JSONResponseEntity(response.getStatus(), (String) request.getAttribute("javax.servlet.error.message"), null);
    }
}
