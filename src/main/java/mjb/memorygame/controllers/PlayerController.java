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
import mjb.memorygame.repositories.PlayerRepository;

@RestController
public class PlayerController {

	@Autowired
	private PlayerRepository playerRepository;

	@RequestMapping(value = "/api/player", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<?> createPlayer(@RequestParam String name) {
		Player player = new Player(name);
		try {
			player = playerRepository.save(player);
		} 
		catch (RuntimeException e) {
			String msg = "Could not create player: %s.";
			return new ResponseEntity<>(new RestError(String.format(msg, name)), HttpStatus.OK);
		}
		return new ResponseEntity<>(player, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/players", method = RequestMethod.GET)
	ResponseEntity<?> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		players = playerRepository.findAll();
		return new ResponseEntity<>(players, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/playersbyname", method = RequestMethod.GET)
	ResponseEntity<?> getPlayersByName(@RequestParam String name) {
		List<Player> players = playerRepository.findByName(name);
		return new ResponseEntity<>(players, HttpStatus.OK);
	}

}