package com.example.messenger.chat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ChatMessage {

  @Id
  @GeneratedValue
  private Long id;
  private String fromUser;
  private String toUser;
  private String content;
  private long timestamp;
  private Long replyToMessageId;
}