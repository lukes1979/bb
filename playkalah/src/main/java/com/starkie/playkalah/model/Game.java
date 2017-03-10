package com.starkie.playkalah.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Model class to represent a game of kalah
 * @author luke.starkie
 */
public class Game {
	
	private static final Logger logger = LoggerFactory.getLogger(Game.class);
	
	private static final int STARTING_STONE_COUNT = 6;
	private static final int TOTAL_PITS = 6;

	/**
	 * The unique identifier for the game
	 */
	private String id;
	
	/**
	 * The next player to move
	 */
	private String nextPlayerNameToMove;

	/**
	 * Player 1 of the game
	 */
	private Player player1;
	
	/**
	 * Player 2 of the game
	 */
	private Player player2;
	
	/**
	 * Create a game, the number of pits could be flexible to support any type of game kalah(x,y)
	 * but hard coding to 6 for this sample code.  Player1 is first to move (nextPlayerToMove).
	 * Game ID would typically be a random ID (UUID.randomUUID().toString()) but for ease in 
	 * demonstration setting this to 1.
	 * @param player1Name the name of player 1
	 * @param player2Name the name of player 2
	 */
	public Game(String player1Name, String player2Name, String id) {
		this.id = id;
		this.player1 = new Player(player1Name, TOTAL_PITS, STARTING_STONE_COUNT);
		this.player2 = new Player(player2Name, TOTAL_PITS, STARTING_STONE_COUNT);
		this.nextPlayerNameToMove = player1.getName();
	}
	
	public String getId() {
		return id;
	}

	public boolean isGameCompleted() {
		return player1.isPitsEmpty() || player2.isPitsEmpty();
	}
	
	public String getNextPlayerToMove() {
		return nextPlayerNameToMove;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}
	
	/**
	 * Find a player based on the name.
	 * @param playerName name of the player to find
	 * @return
	 */
	public Player findPlayer(String playerName) {
		if (StringUtils.pathEquals(playerName, player1.getName())) {
			return player1;
		} else if (StringUtils.pathEquals(playerName, player2.getName())) {
			return player2;
		} else {
			throw new IllegalArgumentException("Invalid player");
		}
	}
	
	/**
	 * Returns the other player than the one passed in.
	 * @param player the current player
	 * @return the other player
	 */
	private Player findOtherPlayer(Player player) {
		return player == player1 ? player2 : player1;
	}
	
	/**
	 * Check if any valid moves remain, if not ensure all stones are in the correct place.
	 */
	private void checkIfAnyValidMovesRemain() {
		logger.info("Checking if any valid moves remain for user {}", nextPlayerNameToMove);
		Player nextPlayerToMove = findPlayer(nextPlayerNameToMove);
		if (nextPlayerToMove.isPitsEmpty()) {
			findOtherPlayer(nextPlayerToMove).movePitStonesToKalah();
		}
	}

	/**
	 * Plays a move for the player specified on the selected pit.
	 * Rules for algorithm below can be found at https://en.wikipedia.org/wiki/Kalah
	 * @param playerName the player playing the move
	 * @param pitNumber the pit selected to play
	 */
	public void playPit(String playerName, int pitNumber) {
		Player playerInstigatingMove = findPlayer(playerName);
		int stonesInPlayerSelectedPit = playerInstigatingMove.pickupStonesFromPit(pitNumber);

		Player playerSideInAction = playerInstigatingMove;
		int pitToAddStoneTo = pitNumber;
		for (int currentStone = 1; currentStone <= stonesInPlayerSelectedPit; currentStone++) {

			if (pitToAddStoneTo < TOTAL_PITS) {
				logger.info("Same side move on player side {}", playerSideInAction.getName());
				playerSideInAction.addStoneToPit(++pitToAddStoneTo);
				if (currentStone == stonesInPlayerSelectedPit) {
					checkLastStoneRule(playerInstigatingMove, playerSideInAction, pitToAddStoneTo);
				}
			} else if (playerSideInAction == playerInstigatingMove) {
				logger.info("End pit passed on same side as player instigating move, add to kalah");
				playerSideInAction.addStoneToKalah();
				playerSideInAction = findOtherPlayer(playerSideInAction);
				
				// just used the last stone in the kalah, check if the game is complete, return after so we leave the same player in play
				if (currentStone == stonesInPlayerSelectedPit) {
					checkIfAnyValidMovesRemain();
					return;
				}
				pitToAddStoneTo = 0;
			} else {
				logger.info("End pit passed on different side as player instigating move, switching sides in play");
				playerSideInAction = findOtherPlayer(playerSideInAction);
				playerSideInAction.addStoneToPit(1);
				pitToAddStoneTo = 1;
				if (currentStone == stonesInPlayerSelectedPit) {
					checkLastStoneRule(playerInstigatingMove, playerSideInAction, pitToAddStoneTo);
				}
			}
		}
		// flip the next player to move but finally check if any moves are available to see the game has ended
		nextPlayerNameToMove = findOtherPlayer(playerInstigatingMove).getName();
		checkIfAnyValidMovesRemain();
	}

	/**
	 * Check if the last stone dropped is on the same side as the player instigating the move.
	 * If so and the pot contains 1 stone take the stone and opposite players pits stones and place in kalah
	 * @param playerInstigatingMove player who played the move
	 * @param playerSideInAction the side we are move stones on currently
	 * @param lastPitUsed the last pit used
	 */
	private void checkLastStoneRule(Player playerInstigatingMove, Player playerSideInAction, int lastPitUsed) {
		logger.info("Checking for last stone rule");
		if (playerInstigatingMove == playerSideInAction) {
			Pit pitLastUsed = playerInstigatingMove.getPit(lastPitUsed);
			if (pitLastUsed.getNumberOfStonesInPit() == 1) {
				logger.info("Last stone rule in play");
				pitLastUsed.pickupStones();
				playerInstigatingMove.addStoneToKalah();
				
				int opponentStones = findOtherPlayer(playerInstigatingMove).getOppositePitStones(lastPitUsed);
				playerInstigatingMove.addOpponentsStonesToKalah(opponentStones);
			}
		}
	}

	@Override
	public String toString() {
		return "[id=" + id + ", nextPlayer=" + nextPlayerNameToMove + ", player1=" + player1 + ", player2="+ player2 + "]";
	}
}