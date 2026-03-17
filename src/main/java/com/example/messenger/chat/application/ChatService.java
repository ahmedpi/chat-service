package com.example.messenger.chat.application;

import com.example.messenger.chat.adapters.messaging.ChatMessageEvent;
import com.example.messenger.chat.application.port.ChatMessageRepository;
import com.example.messenger.chat.domain.ChatMessage;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

  private final ChatMessageRepository repository;

  public ChatService(ChatMessageRepository repository) {
    this.repository = repository;
  }

  public ChatMessage save(ChatMessageEvent event) {
    ChatMessage message = new ChatMessage();
    message.setFromUser(event.getFromUser());
    message.setToUser(event.getToUser());
    message.setContent(event.getContent());
    message.setTimestamp(event.getTimestamp());
    message.setReplyToMessageId(event.getReplyToMessageId());
    return repository.save(message);
  }

  public List<ChatMessage> getHistory(String user1, String user2) {
//    return repository.findByFromUserAndToUser(fromUser, toUser);
    return repository.findConversation(user1, user2);
  }

  public List<ChatMessage> getReplies(Long messageId) {
    return repository.findByReplyToMessageId(messageId);
  }
}