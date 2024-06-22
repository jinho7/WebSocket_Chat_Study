package com.example.template.domain.chat.entity;

import com.example.template.domain.chat.entity.enums.MessageType;
import com.example.template.global.common.BaseEntity;
import com.example.template.global.config.WebSocketConfig;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "chat_room")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoom extends BaseEntity {

    @Id
    private String id;

    private String name;

    @PrePersist
    public void generateId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> participants = new ArrayList<>();

//    private Set<WebSocketSession> sessions = new HashSet<>();


    public void addParticipant(Member member) {
        if (participants.size() < 4) {
            participants.add(member);
            member.setChatRoom(this);
        } else {
            throw new IllegalStateException("Chat room is full. Maximum 4 participants allowed.");
        }
    }

    public void removeParticipant(Member member) {
        participants.remove(member);
        member.setChatRoom(null);
    }
}