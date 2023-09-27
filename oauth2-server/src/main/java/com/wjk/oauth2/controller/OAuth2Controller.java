package com.wjk.oauth2.controller;

import com.wjk.oauth2.service.RedisService;
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
@RequestMapping("oauth2")
public class OAuth2Controller {

    @Autowired
    private RedisService redisService;

    private Map<String, Map<String,String>> clientModeMap = new HashMap<String, Map<String, String>>();

    public OAuth2Controller(){
        Map<String,String> clientMode = new HashMap<String, String>();
        clientMode.put("clientId","myClientId");
        clientMode.put("clientSecret","myClientSecret");
        clientMode.put("authUrl","http://localhost:9001/auth");
        clientModeMap.put("myClientId",clientMode);
    }


    @GetMapping("login")
    @ResponseBody
    public String login(String username,String password){
        if("test".equals(username)){
            redisService.setCacheObject("mytoken","name:wang,age:22",30L, TimeUnit.MINUTES);
            return "login success";
        }
        return "login fail";
    }

    @GetMapping("authorize")
    public String authorize(String response_type,String client_id,String redirect_url){
        if("code".equals(response_type)){
            String token = "mytoken";
            Object cacheObject = redisService.getCacheObject(token);
            if(cacheObject!=null){
                Map<String, String> clientMode = clientModeMap.get(client_id);
                if(clientMode!=null){
                    String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-","");
                    redisService.setCacheObject(code,token,5L,TimeUnit.MINUTES);
                    String authUrl = clientMode.get("authUrl");
                    return "redirect:"+authUrl+"?code="+code+"&redirect="+redirect_url;
                }
            }
        }
        return "";
    }

    @GetMapping("token")
    @ResponseBody
    public String token(String grant_type,String client_id,String client_secret,String code){
        Map<String, String> clientMode = clientModeMap.get(client_id);
        if(clientMode==null){
            return "error";
        }

        if(!clientMode.get("clientSecret").equals(client_secret)){
            return "secret error";
        }

        Object cacheObject = redisService.getCacheObject(code);
        if(cacheObject==null){
            return "invalid code";
        }
        redisService.deleteObject(code);
        String result = cacheObject+",";
        return result;
    }
}
