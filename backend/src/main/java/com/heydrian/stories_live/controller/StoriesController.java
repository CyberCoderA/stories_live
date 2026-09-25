package com.heydrian.stories_live.controller;

import org.springframework.web.bind.annotation.RestController;

import com.heydrian.stories_live.response.ResponseHandler;
import com.heydrian.stories_live.services.StoryService;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.heydrian.stories_live.dto.request.CreateStoryRequest;
import com.heydrian.stories_live.dto.response.ApiResponse;

@RestController 
@RequestMapping("/api/stories")
public class StoriesController {
    private final StoryService storyService;

    public StoriesController(StoryService storyService) {
        this.storyService = storyService;
    }

    @PostMapping("/create-story")
    public ResponseEntity<ApiResponse<Void>> createStory(
        @Valid @RequestBody CreateStoryRequest request,
        @AuthenticationPrincipal UserDetails user
    ) {
        storyService.createStory(request, user.getUsername());

        return ResponseHandler.created("Story created successfully");
    }
    
}