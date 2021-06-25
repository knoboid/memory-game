package mjb.memorygame.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import mjb.memorygame.entities.Seek;
import mjb.memorygame.events.WebSocketConfiguration;

@Component
public class SeeksEventHandler {

    private SimpMessagingTemplate websocket;

	@Autowired
	public void EventHandler(SimpMessagingTemplate websocket) {
		this.websocket = websocket;
	}

	public void newSeek(Seek seek) {
		this.websocket.convertAndSend(
            WebSocketConfiguration.MESSAGE_PREFIX + "/newSeek", getPath(seek));
    }

	public void seekAccepted(Seek seek) {
		this.websocket.convertAndSend(
            WebSocketConfiguration.MESSAGE_PREFIX + "/seekAccepted", getPath(seek));
	}

	public void deleteSeek(Long seekId) {
		this.websocket.convertAndSend(
            WebSocketConfiguration.MESSAGE_PREFIX + "/deleteSeek", seekId);
	}
    
	private String getPath(Seek seek) {
		return String.format("%d", seek.getId());
	}
}
