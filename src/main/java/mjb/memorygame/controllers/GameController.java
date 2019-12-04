package mjb.memorygame.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mjb.memorygame.entities.Cards;
import mjb.memorygame.entities.Game;
import mjb.memorygame.entities.Move;
import mjb.memorygame.entities.RestError;
import mjb.memorygame.game.MemoryGame;
import mjb.memorygame.game.exceptions.MemoryGameCardIsFaceUpException;
import mjb.memorygame.game.exceptions.MemoryGameCardOutOfRangeException;
import mjb.memorygame.game.exceptions.MemoryGameLockedException;
import mjb.memorygame.game.exceptions.MemoryGameWrongPlayerException;
import mjb.memorygame.repositories.CardsRepository;
import mjb.memorygame.repositories.GameRepository;
import mjb.memorygame.services.GameServiceImpl;

@RestController
public class GameController {

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private GameRepository gameRepository;

    

    @RequestMapping(value = "/api/move", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> postAddMove(@RequestParam long gameId, @RequestParam long playerId, @RequestParam int cardIndex) {
        Game game = gameRepository.findById(gameId).get();
        int player = gameService.getPlayerNumber(game, playerId);
        if (player == 0) {
            String msg = "%d: no such playerId for the game %d";
            return new ResponseEntity<>(new RestError(String.format(msg, playerId, gameId)), HttpStatus.BAD_REQUEST);
        }

        MemoryGame memoryGame = gameService.loadFromStorage(game);
        if (memoryGame == null) {
            return new ResponseEntity<>(new RestError("Wrong Player"), HttpStatus.BAD_REQUEST);
        }

        try {
            memoryGame.revealCard(player, cardIndex);
        } catch (MemoryGameWrongPlayerException e) {
            return new ResponseEntity<>(new RestError("Wrong Player"), HttpStatus.BAD_REQUEST);
        } catch (MemoryGameCardIsFaceUpException e) {
            return new ResponseEntity<>(new RestError("Card is face up"), HttpStatus.BAD_REQUEST);
        } catch (MemoryGameLockedException e) {
            return new ResponseEntity<>(new RestError("The game is locked"), HttpStatus.BAD_REQUEST);
        } catch (MemoryGameCardOutOfRangeException e) {
            return new ResponseEntity<>(new RestError("Card out of range"), HttpStatus.BAD_REQUEST);
        }

        gameService.populateEntityFromGame(game, memoryGame);
        game.getMoves().add(new Move(cardIndex));
        gameRepository.save(game);

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/game", method = RequestMethod.GET)
    ResponseEntity<?> getViewGame(@RequestParam long id) {
        Game game = gameRepository.findById(id).get();
        List<Move> moves = game.getMoves();
        MemoryGame memoryGame = gameService.loadFromStorage(game);
        if (memoryGame == null) {
            return new ResponseEntity<>(new RestError("Wrong Player"), HttpStatus.BAD_REQUEST);            
        }

        gameService.populateEntityFromGame(game, memoryGame);
        game.setMoves(moves);

		return new ResponseEntity<>(game, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/cards", method = RequestMethod.GET)
	ResponseEntity<?> getViewCards(@RequestParam Long gameId) {
        Cards cards = cardsRepository.findById(gameId).get();
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/completeMove", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> postCompleteMove(@RequestParam long gameId) {
        Game game = gameRepository.findById(gameId).get();
        MemoryGame memoryGame = gameService.loadFromStorage(game);

        if (memoryGame == null) {
            return new ResponseEntity<>(new RestError("Wrong Player"), HttpStatus.BAD_REQUEST);            
        }

        memoryGame.completeTurn();
        gameService.populateEntityFromGame(game, memoryGame);
        gameRepository.save(game);

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

}