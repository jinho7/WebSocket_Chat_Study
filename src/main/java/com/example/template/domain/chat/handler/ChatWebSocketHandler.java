package com.example.template.domain.chat.handler;

import com.example.template.domain.chat.converter.ChatConverter;
import com.example.template.domain.chat.dto.request.ChatRequestDto;
import com.example.template.domain.chat.dto.response.ChatResponseDto;
import com.example.template.domain.chat.entity.enums.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final Map<String, List<WebSocketSession>> webSockets = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatRequestDto chatRequestDto = objectMapper.readValue(message.getPayload(), ChatRequestDto.class);
        MessageType type = chatRequestDto.type();

        switch (type) {
            case JOIN:
                handleJoin(session, chatRequestDto);
                break;
            case CHAT:
                handleChat(session, chatRequestDto, message);
                break;
            case LEAVE:
                handleLeave(session, chatRequestDto);
                break;
        }
    }

    private void handleJoin(WebSocketSession session, ChatRequestDto chatRequestDto) throws IOException {
        String roomId = chatRequestDto.roomId();
        webSockets.computeIfAbsent(roomId, k -> new ArrayList<>());
        List<WebSocketSession> roomSessions = webSockets.get(roomId);

        if (!roomSessions.contains(session)) {
            roomSessions.add(session);
            System.out.println(session + "이 " + roomId + "에 접속하였습니다.");

            ChatResponseDto responseDto = ChatConverter.toResponseDto(chatRequestDto);
            broadcastMessage(roomId, responseDto, session);
        } else {
            System.out.println("이미 존재하는 세션입니다.");
        }
    }

    private void handleChat(WebSocketSession session, ChatRequestDto chatRequestDto, TextMessage originalMessage) throws IOException {
        String roomId = chatRequestDto.roomId();
        if (webSockets.containsKey(roomId)) {
            List<WebSocketSession> roomSessions = webSockets.get(roomId);
            if (roomSessions.contains(session)) {
                ChatResponseDto responseDto = ChatConverter.toResponseDto(chatRequestDto);
                broadcastMessage(roomId, responseDto, session);
            } else {
                System.out.println("JOIN을 먼저 해야합니다.");
            }
        } else {
            System.out.println("존재하지 않는 채팅방입니다.");
        }
    }

    private void handleLeave(WebSocketSession session, ChatRequestDto chatRequestDto) throws IOException {
        String roomId = chatRequestDto.roomId();
        if (webSockets.containsKey(roomId)) {
            List<WebSocketSession> roomSessions = webSockets.get(roomId);
            roomSessions.remove(session);
            System.out.println(session + ": 채팅방에서 나갔습니다.");

            if (roomSessions.isEmpty()) {
                webSockets.remove(roomId);
            }

            ChatResponseDto responseDto = ChatConverter.toResponseDto(chatRequestDto);
            broadcastMessage(roomId, responseDto, null);
        }
    }

    private void broadcastMessage(String roomId, ChatResponseDto responseDto, WebSocketSession senderSession) throws IOException {
        if (webSockets.containsKey(roomId)) {
            TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(responseDto));
            for (WebSocketSession session : webSockets.get(roomId)) {
                if (session.isOpen() && !session.equals(senderSession)) {
                    session.sendMessage(textMessage);
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        for (List<WebSocketSession> roomSessions : webSockets.values()) {
            if (roomSessions.remove(session)) {
                System.out.println(session + ": 연결이 종료되었습니다.");
                break;
            }
        }
    }
}