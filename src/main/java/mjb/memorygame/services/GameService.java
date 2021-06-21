package mjb.memorygame.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mjb.memorygame.entities.Cards;
import mjb.memorygame.entities.Game;
import mjb.memorygame.entities.Player;
import mjb.memorygame.game.MemoryGame;
import mjb.memorygame.game.exceptions.MemoryGameCardIsFaceUpException;
import mjb.memorygame.game.exceptions.MemoryGameCardOutOfRangeException;
import mjb.memorygame.game.exceptions.MemoryGameLockedException;
import mjb.memorygame.game.exceptions.MemoryGameWrongPlayerException;
import mjb.memorygame.repositories.CardsRepository;
import mjb.memorygame.repositories.GameRepository;

@Service
public class GameService {

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private GameRepository gameRepository;
    
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
        game.setGameOver(memoryGame.isGameOver());
        game.setWinner(
            memoryGame.getWinner() == 0 ? null
                : memoryGame.getWinner() == 1 ? game.getPlayer1() 
                : game.getPlayer2()
        );
        game.setCurrentPlayer(memoryGame.getCurrentPlayer() == 1 ? game.getPlayer1() : game.getPlayer2());
    }

    public int getPlayerNumber(Game game, long playerId) {
        if (playerId == game.getPlayer1().getId()) {
            return 1;
        } else if (playerId == game.getPlayer2().getId()) {
            return 2;
        }
        return 0;
    }

    /**
     * Gets the game that a player is involved in.
     * @param playerId
     * @return the game the player is currently involved in, otherwise null.
     */
    public Game getPlayersGame(long playerId) {
        List<Game> games = gameRepository.findAll();
        for(Game game : games) {
            if (game.getPlayer1().getId() == playerId) {
                return game;
            }
            else if (game.getPlayer2().getId() == playerId) {
                return game;
            }
        }
        return null;
    }

    public Game newGame(Player player1, Player player2, int cardCount) {
        Game game = gameRepository.save((new Game(player1, player2, cardCount)));
		MemoryGame memoryGame = new MemoryGame(cardCount);
		Cards cards = new Cards(game.getId(), memoryGame.getCardsAsList());
		cardsRepository.save(cards);
		game.setBoard(memoryGame.getBoardAsList());
        return game;
    }

}