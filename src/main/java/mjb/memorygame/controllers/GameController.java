package mjb.memorygame.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mjb.memorygame.entities.Move;
import mjb.memorygame.entities.Game;

import mjb.memorygame.repositories.GameRepository;


@RestController
public class GameController {

    // @Autowired
	// private PlayerRepository playerRepository;

	@Autowired
	private GameRepository gameRepository;

    // @Autowired
    // private MoveRepository moveRepository;
    
    @RequestMapping(value = "/move", method = RequestMethod.POST)
	@ResponseBody List<Move> postAddSeek(@RequestParam long gameId, @RequestParam long playerId, @RequestParam long cardIndex) {
        Game game = gameRepository.findById(gameId).get();
        List<Move> moves = game.getMoves();
        moves.add(new Move(cardIndex));
        gameRepository.save(game);
		return moves;
    }
    
    @RequestMapping(value = "/game", method = RequestMethod.GET)
	Game getViewGame(@RequestParam long id) {
        Game game = gameRepository.findById(id).get();
		return game;
	}

}