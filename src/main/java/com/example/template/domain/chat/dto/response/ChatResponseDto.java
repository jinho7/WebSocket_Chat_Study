package com.example.template.domain.chat.dto.response;

import com.example.template.domain.chat.entity.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatResponseDto(
        Long id,
        Long senderId,
        String roomId,

        String username,
        String profileImgUrl,
        MessageType type,

        LocalDateTime createdAt,
        String content

) {
}