package mjb.memorygame.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mjb.memorygame.entities.Cards;
import mjb.memorygame.entities.Game;
import mjb.memorygame.game.MemoryGame;
import mjb.memorygame.game.exceptions.MemoryGameCardIsFaceUpException;
import mjb.memorygame.game.exceptions.MemoryGameCardOutOfRangeException;
import mjb.memorygame.game.exceptions.MemoryGameLockedException;
import mjb.memorygame.game.exceptions.MemoryGameWrongPlayerException;
import mjb.memorygame.repositories.CardsRepository;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private CardsRepository cardsRepository;
    
    public MemoryGame loadFromStorage(Game game) {
        List<Integer> moves = game.asList();
        Cards cards = cardsRepository.findById(game.getId()).get();
        MemoryGame memoryGame = null;
        try {
            memoryGame = new MemoryGame(cards.asList(), moves, game.getLock());
        } catch (MemoryGameWrongPlayerException e) {
            // This should never be thrown when creating a game that is supposedly valid.
            // All moves are pulled from the database and have already been varified.
            e.printStackTrace();
        } catch (MemoryGameCardIsFaceUpException e) {
            // The following applies again:
            // This should never be thrown when creating a game that is supposedly valid.
            // All moves are pulled from the database and have already been varified.
            e.printStackTrace();
        } catch (MemoryGameLockedException e) {
            // The following applies again:
            // This should never be thrown when creating a game that is supposedly valid.
            // All moves are pulled from the database and have already been varified.
            e.printStackTrace();
        } catch (MemoryGameCardOutOfRangeException e) {
            // The following applies again:
            // This should never be thrown when creating a game that is supposedly valid.
            // All moves are pulled from the database and have already been varified.
            e.printStackTrace();
        }
        if (memoryGame == null) {
            return memoryGame;
        }
        memoryGame.setLock(game.getLock());
        return memoryGame;
    }

    public void populateEntityFromGame(Game game, MemoryGame memoryGame) {
        game.setBoard(memoryGame.getBoardAsList());
        game.setLock(memoryGame.getLock());
        game.setScore1(memoryGame.getPlayer1Score());
        game.setScore2(memoryGame.getPlayer2Score());
        game.setSuccessfulTurn(memoryGame.isSuccessfulTurn());
    }

    public int getPlayerNumber(Game game, long playerId) {
        if (playerId == game.getPlayer1().getId()) {
            return 1;
        } else if (playerId == game.getPlayer2().getId()) {
            return 2;
        }
        return 0;
    }

}
