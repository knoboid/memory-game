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

	public void newAccept(Seek seek) {
		this.websocket.convertAndSend(
            WebSocketConfiguration.MESSAGE_PREFIX + "/newAccept", getPath(seek));
	}
    
	private String getPath(Seek seek) {
		return String.format("%d", seek.getId());
	}
}
