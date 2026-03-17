package com.example.messenger.chat.adapters.messaging;

import java.io.Serializable;
import lombok.Data;

@Data
public class ChatMessageEvent implements Serializable {

  private String fromUser;
  private String toUser;
  private String content;
  private long timestamp;
  private Long replyToMessageId; // For replies
}