//package com.example.template.domain.chat.service;
//
//import com.example.template.domain.chat.dto.request.ChatRequestDto;
//import com.example.template.domain.chat.dto.response.ChatResponseDto;
//import com.example.template.domain.chat.entity.Message;
//import com.example.template.domain.chat.repository.MessageRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Map;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class ChatServiceImpl implements ChatService {
//
//    private final MessageRepository messageRepository;
//
//    // 채팅 내역 저장
//    @Override
//    @Transactional
//    public ChatResponseDto save(ChatRequestDto chatRequest, Long crewId, Map<String, Object> header) {
//
//        Message message = chatMapper.toChat(chatRequest, crewId);
//        Message savedChat = messageRepository.save(message);
//
//        return toChatResponse(savedChat, header);
//    }
//}
