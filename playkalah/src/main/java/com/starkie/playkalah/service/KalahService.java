package com.starkie.playkalah.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.starkie.playkalah.model.Game;

/**
 * Service class for the Kalah application.
 * @author luke.starkie
 */
@Service
public class KalahService {
	
	private static final Logger logger = LoggerFactory.getLogger(KalahService.class);
	
	static final String GAME_1 = "1";
	
	/**
	 * Hold a list of games, this would be moved to a database in a production environment.
	 */
	private Map<String, Game> games = new HashMap<String, Game>();
	
	/**
	 * Sets up a sample game
	 */
	@PostConstruct
	public void setupGame1() {
		Game game = new Game("bill", "bob", GAME_1);
		games.put(game.getId(), game);
	}

	/**
	 * Returns the game with the specified ID or a newly created game with the ID if one does not exist.
	 * @param id the game requested
	 * @return
	 */
	public Game findGame(String id) {
		Game game = games.get(id);
		if (game == null) {
			game = new Game("bill", "bob", id);
			games.put(game.getId(), game);
		} 
		return game;
	}
	
	/**
	 * Plays a move in a game of kalah.  Check for error scenarios and throw an appropriate exception.
	 * @param id the game id to play
	 * @param playerName the player playing the move
	 * @param pit the pit selected to play
	 * @return
	 */
	public Game playGame(String id, String playerName, int pitNumber) {
		Game game = games.get(id);
		logger.info("Play game move requested for game {}, player {}, pit {}, game state {}", new Object[]{id, playerName, pitNumber, game});
		
		if (game == null) {
			throw new IllegalArgumentException("Not a valid game");
		} else if(pitNumber < 1 || pitNumber > 6) {
			throw new IllegalArgumentException("Not a valid pit number");
		} else if (game.isGameCompleted()) {
			throw new IllegalStateException("Game has been completed, play another?");
		} else if(!game.getNextPlayerToMove().equals(playerName)) {
			throw new IllegalArgumentException("Not your turn sorry :-)");
		} else if(game.findPlayer(playerName).getPit(pitNumber).isEmpty()) {
			throw new IllegalArgumentException("Pit is empty, try another one!!");
		} else {
			game.playPit(playerName, pitNumber);
			logger.info("Play game move completed with game state {}", game);
			return game;
		}
	}
}
