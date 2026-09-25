package com.heydrian.stories_live.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateChapterRequest(
    @NotBlank String storyId,
    @NotNull @Min(1) Integer chapterNumber,
    @NotBlank @Size(max = 200) String chapterTitle,
    @NotBlank @Size(max = 30000) String chapterContent,
    @Min(0) int chapterWordCount
) {
}