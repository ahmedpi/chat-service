package com.example.messenger.chat.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  @Bean
  public TopicExchange chatExchange() {
    return new TopicExchange("chat.exchange");
  }
  @Bean
  public Queue chatMessageQueue() {
    return new Queue("chat.message.queue");
  }
  @Bean
  public Binding chatMessageBinding(Queue chatMessageQueue, TopicExchange chatExchange) {
    return BindingBuilder.bind(chatMessageQueue).to(chatExchange).with("chat.message");
  }
  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}