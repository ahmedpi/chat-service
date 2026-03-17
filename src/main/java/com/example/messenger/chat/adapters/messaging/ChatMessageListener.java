package com.example.messenger.chat.adapters.messaging;

import com.example.messenger.chat.application.ChatService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageListener {

  private final ChatService chatService;
  private final SimpMessagingTemplate messagingTemplate;

  public ChatMessageListener(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
    this.chatService = chatService;
    this.messagingTemplate = messagingTemplate;
  }

  @RabbitListener(queues = "chat.message.queue")
  public void handleChatMessage(ChatMessageEvent event) {
    chatService.save(event); // Save to DB
    //For Testing, Use Broadcast
    // messagingTemplate.convertAndSend("/queue/messages", event);

    //For Real Private Messaging ( to be used after implementing user authentication)
    // Send to recipient's WebSocket queue
    messagingTemplate.convertAndSendToUser(
        event.getToUser(), "/queue/messages", event
    );
    // Optionally, send to sender as well (for echo/confirmation)
    messagingTemplate.convertAndSendToUser(
        event.getFromUser(), "/queue/messages", event
    );
  }
}