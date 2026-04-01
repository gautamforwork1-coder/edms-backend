package com.edms.edms_backend.service;

import com.edms.edms_backend.dto.request.LoginRequestDto;
import com.edms.edms_backend.dto.request.RegisterRequestDto;
import com.edms.edms_backend.dto.response.AuthResponseDto;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto request);

    AuthResponseDto login(LoginRequestDto request);
}