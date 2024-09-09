package coms309.TriviaBum.Websockets.AdminChat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMessageRepository extends JpaRepository<AdminChatMessage, Long>{

}