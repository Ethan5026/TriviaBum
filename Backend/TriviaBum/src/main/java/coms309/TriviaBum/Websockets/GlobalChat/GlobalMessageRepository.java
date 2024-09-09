package coms309.TriviaBum.Websockets.GlobalChat;

import coms309.TriviaBum.Websockets.AdminChat.AdminChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalMessageRepository extends JpaRepository<GlobalChatMessage, Long>{

}