package com.heydrian.stories_live.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heydrian.stories_live.dto.request.CreateStoryRequest;
import com.heydrian.stories_live.dto.response.ApiResponse;
import com.heydrian.stories_live.dto.response.StoryResponse;
import com.heydrian.stories_live.response.ResponseHandler;
import com.heydrian.stories_live.services.StoryService;

import jakarta.validation.Valid;

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

    @GetMapping("/getStory/{storyId}")
    public ResponseEntity<ApiResponse<StoryResponse>> getStory(
        @PathVariable String storyId,
        @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseHandler.success(
            HttpStatus.OK,
            "Story retrieved successfully",
            StoryResponse.fromStory(storyService.getStory(storyId, user.getUsername()))
        );
    }
}