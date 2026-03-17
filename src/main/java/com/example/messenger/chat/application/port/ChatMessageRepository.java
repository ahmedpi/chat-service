package com.example.messenger.chat.application.port;

import com.example.messenger.chat.domain.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

//  List<ChatMessage> findByFromUserAndToUser(String fromUser, String toUser);
  @Query("SELECT m FROM ChatMessage m WHERE (m.fromUser = :user1 AND m.toUser = :user2) OR (m.fromUser = :user2 AND m.toUser = :user1) ORDER BY m.timestamp")
  List<ChatMessage> findConversation(@Param("user1") String user1, @Param("user2") String user2);


  List<ChatMessage> findByReplyToMessageId(Long replyToMessageId);
}
