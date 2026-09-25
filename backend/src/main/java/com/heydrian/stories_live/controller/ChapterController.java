package com.heydrian.stories_live.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heydrian.stories_live.dto.request.CreateChapterRequest;
import com.heydrian.stories_live.dto.request.UpdateChapterRequest;
import com.heydrian.stories_live.dto.response.ApiResponse;
import com.heydrian.stories_live.dto.response.ChapterResponse;
import com.heydrian.stories_live.response.ResponseHandler;
import com.heydrian.stories_live.services.ChapterService;

@RestController
@RequestMapping("/api/stories/chapters")
public class ChapterController {
    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ChapterResponse>> createChapter(
        @Valid @RequestBody CreateChapterRequest request,
        @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseHandler.created("Chapter created successfully", ChapterResponse.fromChapter(
            chapterService.create(request, user.getUsername())));
    }

    @GetMapping("/getChapter/{chapterId}")
    public ResponseEntity<ApiResponse<ChapterResponse>> getChapterById(
        @PathVariable String chapterId,
        @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseHandler.success(HttpStatus.OK, "Chapter retrieved successfully",
            ChapterResponse.fromChapter(chapterService.get(chapterId, user.getUsername())));
    }

    @GetMapping("/all/{storyId}")
    public ResponseEntity<ApiResponse<List<ChapterResponse>>> listByStory(
        @PathVariable String storyId,
        @AuthenticationPrincipal UserDetails user
    ) {
        List<ChapterResponse> chapters = chapterService.listByStory(storyId, user.getUsername())
            .stream().map(ChapterResponse::fromChapter).toList();
        return ResponseHandler.success(HttpStatus.OK, "Chapters retrieved successfully", chapters);
    }

    @PutMapping("/{chapterId}")
    public ResponseEntity<ApiResponse<ChapterResponse>> updateChapter(
        @PathVariable String chapterId,
        @Valid @RequestBody UpdateChapterRequest request,
        @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseHandler.success(HttpStatus.OK, "Chapter updated successfully",
            ChapterResponse.fromChapter(chapterService.update(chapterId, request, user.getUsername())));
    }

    @PatchMapping("/{chapterId}/publish")
    public ResponseEntity<ApiResponse<ChapterResponse>> publishChapter(
        @PathVariable String chapterId,
        @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseHandler.success(HttpStatus.OK, "Chapter published successfully",
            ChapterResponse.fromChapter(chapterService.publish(chapterId, user.getUsername())));
    }

    @DeleteMapping("/{chapterId}")
    public ResponseEntity<ApiResponse<Void>> deleteChapter(
        @PathVariable String chapterId,
        @AuthenticationPrincipal UserDetails user
    ) {
        chapterService.delete(chapterId, user.getUsername());
        return ResponseHandler.success(HttpStatus.NO_CONTENT, "Chapter deleted successfully");
    }
}
