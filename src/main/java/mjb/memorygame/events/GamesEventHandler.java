package mjb.memorygame.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import mjb.memorygame.entities.Game;
import mjb.memorygame.events.WebSocketConfiguration;

@Component
public class GamesEventHandler {
    private SimpMessagingTemplate websocket;

	@Autowired
	public void EventHandler(SimpMessagingTemplate websocket) {
		this.websocket = websocket;
	}

	public void newMove(Game game) {
		this.websocket.convertAndSend(
            WebSocketConfiguration.MESSAGE_PREFIX + "/newMove", getPath(game));
	}
    
	private String getPath(Game game) {
		return String.format("%d", game.getId());
	}
}
