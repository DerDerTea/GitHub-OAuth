package com.example.minimuldemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dengpengfei
 */
@SuppressWarnings("unchecked")
@Controller
public class GithubLoginController {
    @Autowired
    RestTemplate restTemplate;


    @GetMapping("/github.html")
    public String index(){
        return "github";
    }


    @GetMapping("/main.html")
    public String authorizationCode(String code, String state) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "xxxxxxxxxxxx");//填你自己的 client_id
        map.put("client_secret", "xxxxxxxxxxxxxxx");//填你自己的 client_secret
        map.put("state", state);
        map.put("code", code);
        map.put("redirect_uri", "http://localhost:8080/main.html");
        Map<String,String> resp = restTemplate.postForObject("https://github.com/login/oauth/access_token", map, Map.class);
        System.out.println(resp);
        HttpHeaders httpheaders = new HttpHeaders();
        assert resp != null;
        httpheaders.add("Authorization", "token " + resp.get("access_token"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpheaders);
        ResponseEntity<Map> exchange = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, httpEntity, Map.class);
        System.out.println("exchange.getBody() = " + new ObjectMapper().writeValueAsString(exchange.getBody()));
        return "main";
    }
}