package com.wjk.oauth2.controller;

import com.wjk.oauth2.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class OAuth2Controller {


    @GetMapping("auth")
    @ResponseBody
    public String auth(String code,String redirect) throws Exception {
       String result =   HttpClientUtil.doGet("http://localhost:9000/oauth2/token?code="+code+"&client_id=myClientId&client_secret=myClientSecret");
       return result + redirect;
    }

}
