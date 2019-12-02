package mjb.memorygame.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mjb.memorygame.entities.Player;
import mjb.memorygame.entities.Seek;
import mjb.memorygame.game.MemoryGame;
import mjb.memorygame.entities.Cards;
import mjb.memorygame.entities.Game;
import mjb.memorygame.repositories.PlayerRepository;
import mjb.memorygame.repositories.SeekRepository;
import mjb.memorygame.repositories.CardsRepository;
import mjb.memorygame.repositories.GameRepository;

@RestController
public class SeekController {

	@Autowired
    private SeekRepository seekRepository;

    @Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private CardsRepository cardsRepository;
	
	/**
	 * Allows a player to seek a game with other users.
	 * This is essentially a way of advertising willingness to play.
	 * @param seekerId
	 * @param cards
	 * @return
	 */
    @RequestMapping(value = "/seek", method = RequestMethod.POST)
	@ResponseBody Game postAddSeek(@RequestParam Long playerId, @RequestParam int cardCount) {
		Player seeker = playerRepository.findById(playerId).get();
		Seek seek = new Seek(seeker, cardCount);
		Game game = gameRepository.save((new Game(seeker, cardCount)));
		seek.setGame(game);
		seek = seekRepository.save(seek);
		return game;
	}

	@RequestMapping(value = "/seeks", method = RequestMethod.GET)
	List<Seek> getViewSeeks() {
		List<Seek> seeks = new ArrayList<Seek>();
		seeks = seekRepository.findAll();
		return seeks;
	}

    @RequestMapping(value = "/accept", method = RequestMethod.POST)
	@ResponseBody Game postAddAccept(@RequestParam Long seekId, @RequestParam Long playerId) {
		Seek seek = seekRepository.findById(seekId).get();
		Player seeker = playerRepository.findById(playerId).get();
		seek.setAccepter(seeker);
		Game game = seek.getGame();
		MemoryGame memoryGame = new MemoryGame(game.getCardCount());
		Cards cards = new Cards(game.getId(), memoryGame.getCardsAsList());
		cardsRepository.save(cards);
		game.setPlayer2(seeker);
		game.setBoard(memoryGame.getBoardAsList());
		seek = seekRepository.save(seek);
		return game;
	}

}