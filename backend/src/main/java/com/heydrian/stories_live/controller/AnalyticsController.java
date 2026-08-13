package com.heydrian.stories_live.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    // Check if API is running
    @GetMapping("/status")
    public Map<String, String> status() {
        return Map.of("status", "API is running!");
    }
}
