package com.rong.applicationservice.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class ApplicationController {

    @PostConstruct
    public void init() {
        System.out.println("✅ HelloController loaded");
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from application-service";
    }
}
