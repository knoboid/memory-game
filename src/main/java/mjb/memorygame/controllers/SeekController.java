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
import mjb.memorygame.entities.Seek;
import mjb.memorygame.entities.Game;
import mjb.memorygame.repositories.PlayerRepository;
import mjb.memorygame.repositories.SeekRepository;
import mjb.memorygame.services.GameService;
import mjb.memorygame.services.SeekService;
import mjb.memorygame.repositories.CardsRepository;
import mjb.memorygame.repositories.GameRepository;
import mjb.memorygame.events.SeeksEventHandler;

@RestController
public class SeekController {

	@Autowired
    private SeekRepository seekRepository;

    @Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private SeekService seekService;

	@Autowired
	private GameService gameService;

	@Autowired
	private SeeksEventHandler seeksEventHandler;
	
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
		seek = seekRepository.save(seek);
		seeksEventHandler.newSeek(seek);
		return new ResponseEntity<>(seek, HttpStatus.OK); // find out what happens to this response.
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

	@RequestMapping(value = "/api/seek", method = RequestMethod.DELETE)
	ResponseEntity<?> deleteSeek(@RequestParam Long id) {
		seekRepository.deleteById(id);
		seeksEventHandler.deleteSeek(id);
		return new ResponseEntity<>(null, HttpStatus.OK);
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
		Player seeker = seek.getSeeker();
		long seekerId = seeker.getId();
		if (seekService.isSeekAccepted(seek)) {
			String msg = "This seek has already been accepted.";
            return new ResponseEntity<>(new RestError(String.format(msg)), HttpStatus.OK);
		}
		else if (seekerId == playerId) {
			String msg = "You cannot accept your own seeks.";
            return new ResponseEntity<>(new RestError(String.format(msg)), HttpStatus.OK);	
		}
		else {
			boolean accepterIsInAGame = gameService.getPlayersGame(playerId) != null;
			if (accepterIsInAGame) {
				String msg = "You are already in a game.";
				return new ResponseEntity<>(new RestError(String.format(msg)), HttpStatus.OK);
			}
			boolean seekerIsInAGame = gameService.getPlayersGame(seekerId) != null;
			if (seekerIsInAGame) {
				String msg = "That player is already in a game.";
				return new ResponseEntity<>(new RestError(String.format(msg)), HttpStatus.OK);
			}
		}
		Player accepter = playerRepository.findById(playerId).get();
		Game game = gameService.newGame(seeker, accepter, seek.getCards());
		seek.setGame(game);
		seek.setAccepter(accepter);
		seek = seekRepository.save(seek);
		seeksEventHandler.newAccept(seek);
		return new ResponseEntity<>(game, HttpStatus.OK);
	}

}