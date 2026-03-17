# Simplified Messenger-like Chat Service
## Overview
A real-time, scalable chat microservice inspired by Messenger, built with Spring Boot, WebSocket, RabbitMQ, and hexagonal architecture.
Supports user-to-user messaging, message replies, message history, and is ready for extension (group chat, authentication, etc.).

## Architecture
- Hexagonal (Ports & Adapters): Clean separation of business logic, web, messaging, and persistence.
- WebSocket (STOMP): Real-time, bidirectional communication with clients (users).
- RabbitMQ: Event-driven, scalable message distribution between service instances.  Used for backend message queuing and processing (e.g., storing chat history, notifications).
- Database (H2): Message persistence for history and offline delivery.
- User-specific delivery: Uses Spring’s /user/queue/messages for efficient, per-user message routing.
- Reply/thread support: Each message can reference another via replyToMessageId.

## Key Features
- Real-time chat with WebSocket (STOMP)
- Scalable, event-driven architecture with RabbitMQ
- User-to-user messaging (private chat)
- Message replies (threading)
- Message history and reply retrieval via REST API
- Clean, maintainable hexagonal architecture
- Easy to extend (group chat, chat rooms, authentication, etc.)

## Summary Diagram
```
[User A] --(WebSocket/STOMP)--> [Spring Boot Backend] --(broadcast)--> [User B]
|                                   |                                   |
|-- SUBSCRIBE /queue/messages ------>|                                   |
|-- SEND /app/chat.send ------------>|                                   |
|                                   |-- convertAndSend /queue/messages->|
|<-- MESSAGE from /queue/messages ---|<-- MESSAGE from /queue/messages--|
```

## Security

This service uses **Spring Security** for authentication and access control.

- **Form login** is used for browser-based access (the chat UI).
- **HTTP Basic authentication** is enabled for `/api/**` endpoints (for API tools like Postman/curl).
- All chat and user endpoints require authentication.
- Users are defined in-memory for demo purposes.

### Default Users

| Username | Password |
|----------|----------|
| alice    | password |
| bob      | password |
| ahmed    | password |

### API Authentication

- For `/api/**` endpoints, use HTTP Basic Auth.
- Example with curl:
    ```
    curl -u alice:password http://localhost:8086/api/users
    ```
- Example in Postman: Use the Authorization tab, select Basic Auth, and enter your credentials.

### UI Authentication

- Open [http://localhost:8086/chat-client.html](http://localhost:8086/chat-client.html) in your browser.
- Log in with one of the default users.

## Getting Started
**Prerequisites**
    - Java 21+
    - Maven
    - Docker & Docker Compose
### 1. Clone the Repository
    ```
    git clone https://github.com/yourusername/chat-service.git
    cd chat-service
    ```
### 2. Build the Service
    ```
    mvn clean package
    ```
### 3. Start RabbitMQ (Docker Compose Example)
Add to your docker-compose.yml:
    ```
    rabbitmq:
      image: rabbitmq:3-management
      ports:
        - "5672:5672"
        - "15672:15672"
    ```
### 4. Start the Chat Service
Add to your docker-compose.yml:

    ```
    chat-service:
      build: .
      ports:
        - "8086:8086"
      depends_on:
        - rabbitmq
    ```

> **Note:**  
> If your Spring Boot app runs on port 8086 inside the container (as in this project), use `"8086:8086"`.  
> If it runs on 8080, use `"8086:8080"` to map host port 8086 to container port 8080.

Then run:

    ```
    docker-compose up --build
    ```
### 5. Access RabbitMQ Management UI
http://localhost:15672 
(user: guest, password: guest)

## Chat UI (HTML Client)
- The project includes a ready-to-use chat client:
  `src/main/resources/static/chat-client.html`
- This page provides a simple, interactive web interface for sending and receiving messages in real time.
- **Features**:
   - User login via Spring Security form login.
   - Dropdown to select a chat recipient.
   - Real-time message exchange using WebSocket/STOMP.
   - Message history display and reply support.
   - Responsive, minimal UI for easy testing and demonstration.
- How to use:
   1. Start the backend and RabbitMQ as described above.
   2. Open http://localhost:8086/chat-client.html in your browser.
   3. Log in with one of the default users (e.g., alice / password).
   4. Open another browser/incognito window to log in as a different user for testing.
   5. Select a recipient, send messages, and see real-time updates and chat history.

> **Note:**
> The HTML client is intended for demo and development purposes.
> You can extend or replace it with your own frontend as needed.

## REST API
- **GET /api/users**: Get a list of all users except the currently authenticated user.  
  _Used to populate the recipient dropdown in the chat UI._

- **GET /api/whoami**: Get the username of the currently authenticated user.  
  _Used by the chat UI to display the logged-in user and for message sending._

- **GET /api/chat/history/{toUser}**: Get full chat history (both directions) between the authenticated user and the specified user.

- **GET /api/chat/replies/{messageId}**: Get replies to a specific message.

## WebSocket API
- **WebSocket endpoint**: /ws-chat (STOMP over SockJS)
- **Send message**:
   Client sends to /app/chat.send (STOMP)
- **Receive messages**:
   Client subscribes to /user/queue/messages (user-specific queue, delivered via convertAndSendToUser)

## WebSocket Client Example (JavaScript with webstomp-client)
javascript:

    ```
    const ws = new WebSocket('ws://localhost:8086/ws-chat');
    const client = webstomp.over(ws);
    
    client.connect({}, function(frame) {
    client.subscribe('/user/queue/messages', function(message) {
    const chatMessage = JSON.parse(message.body);
    // Display chatMessage.content, chatMessage.fromUser, etc.
    });
    });

    // To send a message or reply:
    client.send("/app/chat.send", JSON.stringify({
    fromUser: "alice",
    toUser: "bob",
    content: "Hello, Bob!",
    replyToMessageId: 123 //
    optional
    }), { 'content-type': 'application/json' });
    ```

## Design Notes
- No per-user topic/queue: Uses Spring’s /user/queue/messages for efficient, scalable delivery.
- Broadcasting: The server delivers each message only to the intended recipient (and optionally the sender) in real time.
- Persistence: All messages are stored for history and offline delivery.
- Reply/thread support: replyToMessageId enables threading and quoting.
- Hexagonal architecture: Clean separation of business logic, adapters, and infrastructure.

## Extending the Service
- Add authentication (Spring Security, JWT)
- Add group chat or chat rooms (route messages to multiple users)
- Add message read receipts, typing indicators, or presence
- Use a production database (Postgres, MySQL)
- Deploy to Kubernetes or cloud

## Troubleshooting
- WebSocket connection fails:
    Ensure the chat service is running and accessible at http://localhost:8086/ws-chat.
- RabbitMQ connection fails:
    Ensure RabbitMQ is running and accessible at localhost:5672.
- Messages not delivered:
    Check logs for errors, ensure correct user IDs, and that clients are subscribed to /user/queue/messages.
