package com.pluralsight.conferencedemo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    @Value("${app.version}")
    //means find that property (app.version) in the application properties and set it/inject it in the variable below
    private String appVersion;

    @GetMapping
    @RequestMapping("/")
    public Map getStatus(){
        Map statusMap = new HashMap<String, String>();
        statusMap.put("app-version", appVersion);
        return statusMap;
    }

}
