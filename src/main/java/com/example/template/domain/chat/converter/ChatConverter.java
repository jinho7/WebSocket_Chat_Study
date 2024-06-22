package com.example.template.domain.chat.converter;

import com.example.template.domain.chat.dto.request.ChatRequestDto;
import com.example.template.domain.chat.dto.response.ChatResponseDto;
import com.example.template.domain.chat.entity.ChatRoom;
import com.example.template.domain.chat.entity.Member;
import com.example.template.domain.chat.entity.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class ChatConverter {

    public static ChatResponseDto toResponseDto(ChatRequestDto request) {
        return ChatResponseDto.builder()
                .type(request.type())
                .senderId(request.senderId())
                .roomId(request.roomId())
                .content(request.content())
                .createdAt(LocalDateTime.now())  // 서버 현재 시간
                .build();
    }
}
