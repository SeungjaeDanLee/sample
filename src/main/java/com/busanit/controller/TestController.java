package com.busanit.controller;

import com.busanit.domain.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    String str = "안녕하세요???";

    @GetMapping("/test1")
    public String test1() {
        return str;
    }

    @GetMapping("/test2")
    public String test2() {
        int a = 1;

        String ex = "";
        System.out.println("ex = " + ex);

        return "Hi!!!!";
    }

    @GetMapping("/test3")
    public UserDto test3() {
        UserDto userDto = new UserDto();
        userDto.setAge(20);
        userDto.setName("hoon");

        return userDto;
    }
}




