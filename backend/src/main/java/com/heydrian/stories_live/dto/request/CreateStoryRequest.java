package com.heydrian.stories_live.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateStoryRequest(
    @NotBlank 
    @Size(min = 5, max = 100)
    String title,

    @NotBlank
    @Size(min = 5, max = 250)
    String description,

    List<String> tags
) {}
