package com.example.template.domain.chat.service;

import com.example.template.domain.chat.dto.request.ChatRequestDto;
import com.example.template.domain.chat.dto.response.ChatResponseDto;

import java.util.Map;

public interface ChatService {

    ChatResponseDto save(ChatRequestDto chatRequest, Long crewId, Map<String, Object> header);
}
