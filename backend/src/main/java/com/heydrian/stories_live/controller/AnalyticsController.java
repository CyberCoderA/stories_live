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
        // Timestamp using instant.now() to get the hh:mm:ss format of the current time
        return Map.of("status", "API is running!", "timestamp", java.time.Instant.now().toString());
    }
}
