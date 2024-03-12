package com.example.demo.src.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class ViewController {


        private  final RestTemplate restTemplate;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/success")
    public String success(){
        return "login-success";
    }


    @GetMapping("/kakao/callback")
    public String handleKakaoCallback(@RequestParam("code") String code) {
        // /app/users/kakao 엔드포인트로 POST 요청을 보내기 위한 URL
        String url = "http://localhost:9000/app/users/kakao";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디 설정
        HttpEntity<String> request = new HttpEntity<>(String.format("{\"authorizationCode\":\"%s\"}", code), headers);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // 성공적으로 처리되면, 성공 페이지로 리다이렉트
        if (response.getStatusCode() == HttpStatus.OK) {
            return "redirect:/success";
        } else {
            // 에러 처리 페이지로 리다이렉트
            return "redirect:/error";
        }
    }


}