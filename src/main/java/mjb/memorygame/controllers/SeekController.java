package mjb.memorygame.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mjb.memorygame.entities.Player;
import mjb.memorygame.entities.RestError;
import mjb.memorygame.entities.Seek;
import mjb.memorygame.game.MemoryGame;
import mjb.memorygame.entities.Cards;
import mjb.memorygame.entities.Game;
import mjb.memorygame.repositories.PlayerRepository;
import mjb.memorygame.repositories.SeekRepository;
import mjb.memorygame.services.SeekService;
import mjb.memorygame.repositories.CardsRepository;
import mjb.memorygame.repositories.GameRepository;
import mjb.memorygame.events.GamesEventHandler;
import mjb.memorygame.events.SeeksEventHandler;

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

	@Autowired
	private SeekService seekService;

	@Autowired
	private SeeksEventHandler seeksEventHandler;

	@Autowired
	private GamesEventHandler gamesEventHandler;
	
	/**
	 * Usedd to seek a game with other players.
	 * This is essentially a way of advertising willingness to play.
	 * @param seekerId
	 * @param cards
	 * @return
	 */
    @RequestMapping(value = "/api/seek", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<?> postAddSeek(@RequestParam Long playerId, @RequestParam int cardPairCount) {
		Player seeker = playerRepository.findById(playerId).get();
		Seek seek = new Seek(seeker, cardPairCount);
		Game game = gameRepository.save((new Game(seeker, cardPairCount)));
		seek.setGame(game);
		seek = seekRepository.save(seek);
		seeksEventHandler.newSeek(seek);
		return new ResponseEntity<>(game, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/seeks", method = RequestMethod.GET)
	ResponseEntity<?> getViewSeeks() {
		List<Seek> seeks = new ArrayList<Seek>();
		seeks = seekRepository.findAll();
		return new ResponseEntity<>(seeks, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/seek", method = RequestMethod.GET)
	ResponseEntity<?> getViewSeek(@RequestParam Long id) {
		Seek seek = seekRepository.findById(id).get();
		return new ResponseEntity<>(seek, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/seeksbyseekerid", method = RequestMethod.GET)
	ResponseEntity<?> getViewSeeksBySeeker(@RequestParam long seekerId) {
		List<Seek> seeks = seekRepository.findBySeekerId(seekerId);
		return new ResponseEntity<>(seeks, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/seeksbyseekerid", method = RequestMethod.DELETE)
	ResponseEntity<?> deleteDeleteSeeksBySeeker(@RequestParam long seekerId) {
		seekRepository.deleteBySeekerId(seekerId);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

    @RequestMapping(value = "/api/accept", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<?> postAddAccept(@RequestParam Long seekId, @RequestParam Long playerId) {
		Seek seek = seekRepository.findById(seekId).get();
		if (seekService.isSeekAccepted(seek)) {
			String msg = "This seek has already been accepted.";
            return new ResponseEntity<>(new RestError(String.format(msg)), HttpStatus.OK);
		}
		else if (seek.getSeeker().getId() == playerId) {
			String msg = "You cannot accept your own seeks.";
            return new ResponseEntity<>(new RestError(String.format(msg)), HttpStatus.OK);	
		}
		Player accepter = playerRepository.findById(playerId).get();
		seek.setAccepter(accepter);
		Game game = seek.getGame();
		MemoryGame memoryGame = new MemoryGame(game.getCardPairCount());
		Cards cards = new Cards(game.getId(), memoryGame.getCardsAsList());
		cardsRepository.save(cards);
		game.setPlayer2(accepter);
		game.setBoard(memoryGame.getBoardAsList());
		seek = seekRepository.save(seek);
		seeksEventHandler.newAccept(seek);
		return new ResponseEntity<>(game, HttpStatus.OK);
	}

}