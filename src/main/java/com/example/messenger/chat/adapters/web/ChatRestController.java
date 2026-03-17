package com.example.messenger.chat.adapters.web;

import com.example.messenger.chat.application.ChatService;
import com.example.messenger.chat.domain.ChatMessage;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

  private final ChatService chatService;

  public ChatRestController(ChatService chatService) {
    this.chatService = chatService;
  }

  @GetMapping("/history/{toUser}")
  public List<ChatMessage> getHistory(@AuthenticationPrincipal UserDetails user, @PathVariable String toUser) {
    return chatService.getHistory(user.getUsername(), toUser);
  }

  @GetMapping("/replies/{messageId}")
  public List<ChatMessage> getReplies(@PathVariable Long messageId) {
    return chatService.getReplies(messageId);
  }
}