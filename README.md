# 💬 ChatApp Backend

<div align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white" alt="Hibernate">
  <img src="https://img.shields.io/badge/WebSocket-4A90E2?style=for-the-badge&logo=websocket&logoColor=white" alt="WebSocket">
  <img src="https://img.shields.io/badge/Ant-A81C7D?style=for-the-badge&logo=apache-ant&logoColor=white" alt="Ant">
</div>

<div align="center">
  <h3>🚀 A Real-time Chat Application Backend built with Java EE</h3>
  <p>Featuring WebSocket communication, Hibernate ORM, and RESTful APIs</p>
</div>

---

## 📋 Table of Contents

- [✨ Features](#-features)
- [🏗️ Architecture](#️-architecture)
- [🛠️ Tech Stack](#️-tech-stack)
- [📁 Project Structure](#-project-structure)
- [🔧 Prerequisites](#-prerequisites)
- [⚡ Quick Start](#-quick-start)
- [🎯 API Endpoints](#-api-endpoints)
- [🔌 WebSocket Events](#-websocket-events)
- [💾 Database Schema](#-database-schema)
- [🚀 Deployment](#-deployment)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)

## ✨ Features

### 🌟 Core Features
- **Real-time Messaging** - Instant message delivery using WebSockets
- **User Management** - Complete user registration and authentication system
- **Friend System** - Add and manage friends functionality
- **Online Status** - Real-time user presence tracking
- **Profile Management** - User profile pictures and information
- **Message History** - Persistent chat storage with Hibernate ORM

### 🔒 Security Features
- Session management
- Input validation
- SQL injection protection via Hibernate
- Cross-origin resource sharing (CORS) handling

### 📱 Real-time Features
- Instant message delivery
- Typing indicators support
- Online/offline status updates
- Multi-device synchronization

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Client    │◄──►│   ChatApp API   │◄──►│   MySQL DB      │
│   (Frontend)    │    │   (Backend)     │    │   (Database)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │
         │              ┌─────────────────┐
         └─────────────►│  WebSocket      │
                        │  Server         │
                        └─────────────────┘
```

### 🔄 Data Flow
1. **Client Connection** → WebSocket handshake with user authentication
2. **Message Processing** → Real-time message broadcasting to connected users
3. **Data Persistence** → Hibernate ORM saves messages to MySQL database
4. **API Communication** → RESTful endpoints for user and profile management

## 🛠️ Tech Stack

### Backend Technologies
- **☕ Java EE 8** - Enterprise Java platform
- **🌐 WebSocket API** - Real-time bidirectional communication
- **🗄️ Hibernate ORM** - Object-relational mapping
- **🐬 MySQL** - Primary database
- **📊 GSON** - JSON processing library
- **🏗️ Apache Ant** - Build automation tool

### Development Environment
- **🔧 NetBeans IDE** - Primary development environment
- **🏃 GlassFish Server** - Java EE application server
- **🔍 JPA** - Java Persistence API

## 📁 Project Structure

```
ChatAppBackEnd/
├── 📂 src/
│   ├── 📂 java/
│   │   ├── 📂 controller/           # REST API Controllers
│   │   │   ├── ProfileController.java
│   │   │   ├── UserController.java
│   │   │   └── TestServlet.java
│   │   ├── 📂 entity/               # JPA Entity Classes
│   │   │   ├── BaseEntity.java
│   │   │   ├── User.java
│   │   │   ├── Chat.java
│   │   │   ├── FriendList.java
│   │   │   └── Status.java
│   │   ├── 📂 dto/                  # Data Transfer Objects
│   │   │   └── UserDTO.java
│   │   ├── 📂 socket/               # WebSocket Components
│   │   │   ├── ChatEndPoint.java
│   │   │   ├── ChatService.java
│   │   │   ├── ChatSummary.java
│   │   │   ├── ProfileService.java
│   │   │   └── UserService.java
│   │   └── 📂 hibernate/            # Database Configuration
│   │       └── HibernateUtil.java
│   └── 📂 conf/
│       └── MANIFEST.MF
├── 📂 web/
│   ├── index.html
│   ├── 📂 profile-images/          # User Profile Pictures
│   └── 📂 WEB-INF/
│       └── glassfish-web.xml
├── 📂 build/                       # Compiled Classes
├── 📂 lib/                         # Dependencies
├── 📂 nbproject/                   # NetBeans Project Files
├── build.xml                       # Ant Build Script
└── README.md                       # This file
```

## 🔧 Prerequisites

### System Requirements
- ☕ **Java JDK 8+**
- 🗄️ **MySQL Server 8.0+**
- 🏃 **GlassFish Server 5+**
- 🔧 **NetBeans IDE** (recommended)
- 🐜 **Apache Ant**

### Dependencies
- Hibernate Core
- MySQL Connector/J
- GSON Library
- Java EE API
- WebSocket API

## ⚡ Quick Start

### 1. 📥 Clone the Repository
```bash
git clone https://github.com/himeshhansana/ChatAppBackEnd.git
cd ChatAppBackEnd
```

### 2. 🗄️ Database Setup
```sql
-- Create database
CREATE DATABASE chat_app;
USE chat_app;

-- Tables will be auto-created by Hibernate
```

### 3. ⚙️ Configuration
Update database credentials in `src/java/hibernate.cfg.xml`:
```xml
<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/chat_app</property>
<property name="hibernate.connection.username">your_username</property>
<property name="hibernate.connection.password">your_password</property>
```

### 4. 🚀 Build & Deploy
```bash
# Using Ant
ant clean
ant compile
ant dist

# Deploy to GlassFish
cp dist/ChatAppBackEnd.war $GLASSFISH_HOME/domains/domain1/autodeploy/
```

### 5. 🔗 Access the Application
- **API Base URL**: `http://localhost:8080/ChatAppBackEnd`
- **WebSocket URL**: `ws://localhost:8080/ChatAppBackEnd/chat`

## 🎯 API Endpoints

### 👤 User Management
```http
POST   /api/users/register     # Register new user
POST   /api/users/login        # User authentication
GET    /api/users/profile      # Get user profile
PUT    /api/users/profile      # Update user profile
GET    /api/users/search       # Search users
```

### 👥 Friends Management
```http
POST   /api/friends/add        # Send friend request
POST   /api/friends/accept     # Accept friend request
GET    /api/friends/list       # Get friends list
DELETE /api/friends/remove     # Remove friend
```

### 💬 Chat History
```http
GET    /api/chat/history       # Get chat history
POST   /api/chat/send          # Send message (fallback)
```

### Example API Usage
```javascript
// Register a new user
const response = await fetch('/ChatAppBackEnd/api/users/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    firstName: 'John',
    lastName: 'Doe',
    countryCode: '+1',
    contactNo: '1234567890'
  })
});
```

## 🔌 WebSocket Events

### Connection
```javascript
// Connect to WebSocket
const socket = new WebSocket('ws://localhost:8080/ChatAppBackEnd/chat?userId=123');
```

### 📨 Send Message
```javascript
const message = {
  type: 'SEND_MESSAGE',
  toUserId: 456,
  message: 'Hello there!',
  timestamp: new Date().toISOString()
};
socket.send(JSON.stringify(message));
```

### 📩 Receive Messages
```javascript
socket.onmessage = (event) => {
  const data = JSON.parse(event.data);
  switch(data.type) {
    case 'NEW_MESSAGE':
      displayMessage(data.message);
      break;
    case 'USER_STATUS':
      updateUserStatus(data.userId, data.status);
      break;
  }
};
```

### 🔔 Event Types
| Event Type | Description |
|------------|-------------|
| `SEND_MESSAGE` | Send a chat message |
| `NEW_MESSAGE` | Receive a new message |
| `USER_STATUS` | User online/offline status |
| `TYPING` | Typing indicator |
| `MESSAGE_DELIVERED` | Message delivery confirmation |

## 💾 Database Schema

### 👤 User Table
```sql
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(45) NOT NULL,
    last_name VARCHAR(45) NOT NULL,
    country_code VARCHAR(5) NOT NULL,
    contact_no VARCHAR(45) UNIQUE NOT NULL,
    status ENUM('ONLINE', 'OFFLINE', 'AWAY') DEFAULT 'OFFLINE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 💬 Chat Table
```sql
CREATE TABLE chat (
    id INT AUTO_INCREMENT PRIMARY KEY,
    from_user INT NOT NULL,
    to_user INT NOT NULL,
    message LONGTEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_user) REFERENCES user(id),
    FOREIGN KEY (to_user) REFERENCES user(id)
);
```

### 👥 Friend List Table
```sql
CREATE TABLE friend_list (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'BLOCKED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (friend_id) REFERENCES user(id),
    UNIQUE KEY unique_friendship (user_id, friend_id)
);
```

## 🚀 Deployment

### 🐳 Docker Deployment (Optional)
```dockerfile
# Dockerfile
FROM openjdk:8-jre-alpine
COPY dist/ChatAppBackEnd.war /opt/glassfish/domains/domain1/autodeploy/
EXPOSE 8080 8181
CMD ["asadmin", "start-domain", "--verbose"]
```

### ☁️ Production Checklist
- [ ] Update database credentials
- [ ] Configure HTTPS/WSS
- [ ] Set up load balancing
- [ ] Configure logging
- [ ] Set up monitoring
- [ ] Database backup strategy
- [ ] Security hardening

### 📊 Performance Optimization
- Connection pooling configuration
- Database indexing
- WebSocket connection limits
- Memory management
- Caching strategies

## 🔧 Development

### 🧪 Testing
```bash
# Run unit tests (if available)
ant test

# Integration testing
curl -X POST http://localhost:8080/ChatAppBackEnd/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Test","lastName":"User","contactNo":"1234567890"}'
```

### 🐛 Debugging
- Enable Hibernate SQL logging in `hibernate.cfg.xml`
- Use GlassFish admin console for monitoring
- Check server logs in `glassfish/domains/domain1/logs/`

### 📈 Monitoring
- GlassFish monitoring console
- Database connection pool monitoring
- WebSocket connection tracking
- Memory usage monitoring

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **🍴 Fork the repository**
2. **🌿 Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **💾 Commit changes**: `git commit -m 'Add amazing feature'`
4. **📤 Push to branch**: `git push origin feature/amazing-feature`
5. **🔄 Create Pull Request**

### 📝 Coding Standards
- Follow Java naming conventions
- Add JavaDoc comments for public methods
- Use proper exception handling
- Write unit tests for new features
- Follow REST API best practices

### 🐛 Bug Reports
Please include:
- Steps to reproduce
- Expected vs actual behavior
- Environment details
- Error logs/stack traces

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">
  <h3>🌟 Star this project if you find it helpful!</h3>
  <p>Made with ❤️ by <a href="https://github.com/himeshhansana">Himesh Hansana</a></p>
  
  <div>
    <a href="https://github.com/himeshhansana/ChatAppBackEnd/issues">🐛 Report Bug</a>
    ·
    <a href="https://github.com/himeshhansana/ChatAppBackEnd/issues">✨ Request Feature</a>
    ·
    <a href="https://github.com/himeshhansana/ChatAppBackEnd/discussions">💬 Discussions</a>
  </div>
</div>

## 📞 Contact & Support

- **📧 Email**: himeshhansana@gmail.com
- **🐙 GitHub**: [@himeshhansana](https://github.com/himeshhansana)
- **💼 LinkedIn**: [Connect with me](https://www.linkedin.com/in/himesh-hansana)

---

### 🚀 Ready to build amazing chat applications? Let's get started!