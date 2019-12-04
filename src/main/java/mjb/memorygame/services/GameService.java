package mjb.memorygame.services;

import mjb.memorygame.entities.Game;
import mjb.memorygame.game.MemoryGame;

public interface GameService {

    public MemoryGame loadFromStorage(Game game);

    public void populateEntityFromGame(Game game, MemoryGame memoryGame);

    public int getPlayerNumber(Game game, long playerId);

}