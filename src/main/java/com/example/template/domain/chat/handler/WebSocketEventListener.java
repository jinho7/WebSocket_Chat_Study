//package com.example.template.domain.chat.handler;
//
//import com.example.template.domain.chat.dto.request.ChatRequestDto;
//import com.example.template.domain.chat.entity.ChatRoom;
//import com.example.template.domain.chat.entity.Member;
//import com.example.template.domain.chat.entity.enums.MessageType;
//import com.example.template.domain.chat.repository.ChatRoomRepository;
//import com.example.template.domain.chat.repository.MemberRepository;
//import com.example.template.global.apiPayload.code.GeneralErrorCode;
//import com.example.template.global.apiPayload.exception.GeneralException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.messaging.SessionConnectedEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//import org.springframework.web.socket.messaging.SessionSubscribeEvent;
//
//import java.util.Map;
//import java.util.Objects;
//
//@Component
//@RequiredArgsConstructor
//@EnableWebSocketMessageBroker
//@Slf4j
//public class WebSocketEventListener {
//
//    // TODO :  ROOMID UUID로 변경
//
//    private final SimpMessageSendingOperations messagingTemplate;
//    private final ChatRoomRepository chatRoomRepository;
//    private final MemberRepository memberRepository;
//
//    // 연결 요청
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        log.info("Received a new web socket connection");
//    }
//
//    // 구독 요청(입장)
//    @EventListener
//    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = (String) getValue(accessor, "username");
//        Long userId = (Long) getValue(accessor, "userId");
//        String roomId = (String) getValue(accessor, "roomId");
//
//        log.info("User: {} {} Subscribe ChatRoom : {}", userId, username, roomId);
//
//        Member member = memberRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
//
//        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
//
//        // 채팅방에 멤버 추가
//        if (chatRoom.getParticipants().size() < 4) {
//            chatRoom.addParticipant(member);
//            chatRoomRepository.save(chatRoom);
//
//            ChatRequestDto chatRequest = ChatRequestDto.builder()
//                    .type(MessageType.JOIN)
//                    .senderId(userId)
//                    .roomId("room1")
//                    .content(username + " 님이 입장했습니다.")
//                    .build();
//
//            messagingTemplate.convertAndSend("/topic/chat/" + roomId, chatRequest);
//        } else {
//            log.warn("ChatRoom {} is full. User {} cannot join.", roomId, userId);
//        }
//    }
//
//    // 연결 해제
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = (String) getValue(accessor, "username");
//        Long userId = (Long) getValue(accessor, "userId");
//        String roomId = (String) getValue(accessor, "roomId");
//
//        log.info("User: {} {} Disconnected ChatRoom : {}", userId, username, roomId);
//
//        Member member = memberRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
//
//        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
//
//        // 채팅방에서 멤버 제거
//        chatRoom.removeParticipant(member);
//        chatRoomRepository.save(chatRoom);
//
//        // 채팅방에 참여자가 없으면 채팅방 삭제
//        if (chatRoom.getParticipants().isEmpty()) {
//            chatRoomRepository.delete(chatRoom);
//        }
//
//        ChatRequestDto chatRequest = ChatRequestDto.builder()
//                .type(MessageType.LEAVE)
//                .senderId(userId)
//                .roomId("room1")
//                .content(username + " 님이 떠났습니다.")
//                .build();
//
//        messagingTemplate.convertAndSend("/topic/chat/" + roomId, chatRequest);
//    }
//
//    private Object getValue(StompHeaderAccessor accessor, String key) {
//        Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
//        Object value = sessionAttributes.get(key);
//        if (Objects.isNull(value)) {
//            throw new GeneralException(GeneralErrorCode.BAD_REQUEST_400);
//        }
//        return value;
//    }
//
//    private Map<String, Object> getSessionAttributes(StompHeaderAccessor accessor) {
//        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
//        if (Objects.isNull(sessionAttributes)) {
//            throw new GeneralException(GeneralErrorCode.BAD_REQUEST_400);
//        }
//        return sessionAttributes;
//    }
//}