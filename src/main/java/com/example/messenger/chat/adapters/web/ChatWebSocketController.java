package com.example.messenger.chat.adapters.web;

import com.example.messenger.chat.adapters.messaging.ChatMessageEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

  private final RabbitTemplate rabbitTemplate;
  private final SimpMessagingTemplate messagingTemplate;

  public ChatWebSocketController(RabbitTemplate rabbitTemplate, SimpMessagingTemplate messagingTemplate) {
    this.rabbitTemplate = rabbitTemplate;
    this.messagingTemplate = messagingTemplate;
  }

  @MessageMapping("/chat.send")
  public void sendMessage(ChatMessageEvent event) {
    System.out.println("Received message: " + event);
    event.setTimestamp(System.currentTimeMillis());
    rabbitTemplate.convertAndSend("chat.exchange", "chat.message", event);
    // Broadcast to WebSocket subscribers (HTML client)
    // messagingTemplate.convertAndSend("/queue/messages", event);
  }
}