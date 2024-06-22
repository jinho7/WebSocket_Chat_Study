package com.example.template.domain.chat.dto.request;

import com.example.template.domain.chat.entity.enums.MessageType;
import lombok.Builder;

@Builder
public record ChatRequestDto(
        MessageType type,
        Long senderId,
        String roomId,
        String content
) {
}