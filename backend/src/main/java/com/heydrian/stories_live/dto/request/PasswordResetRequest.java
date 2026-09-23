package com.heydrian.stories_live.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetRequest (
    @NotBlank String selector,
    @NotBlank String token,
    @NotBlank @Size(min = 8, max = 72) String newPassword
){}
