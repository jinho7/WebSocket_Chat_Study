//package com.example.template.domain.chat.controller;
//
//import com.example.template.domain.chat.dto.request.ChatRequestDto;
//import com.example.template.domain.chat.dto.response.ChatResponseDto;
//import com.example.template.domain.chat.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.handler.annotation.*;
//import org.springframework.stereotype.Controller;
//
//import java.util.Map;
//
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//public class ChatController {
//
//    private final ChatService chatService;
//
//    @MessageMapping("/chat.sendMessage/{crewId}")
//    @SendTo("/topic/public/{crewId}")
//    public ChatResponseDto sendMessage(@DestinationVariable Long roomId,
//                                       @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
//                                       @Payload ChatRequestDto chatRequestDto
//    ) {
//        return chatService.save(chatRequestDto, roomId, simpSessionAttributes);
//    }
//}