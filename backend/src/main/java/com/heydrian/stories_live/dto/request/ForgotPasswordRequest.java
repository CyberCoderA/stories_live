package com.heydrian.stories_live.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest (
    @NotBlank @Email String email
){}