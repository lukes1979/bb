package com.starkie.playkalah.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.starkie.playkalah.model.Game;

/**
 * Test class for the Kalah service.
 * @author luke.starkie
 */
public class KalahServiceTest {
	
	private KalahService service;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	
	/**
	 * Set up a new game in a consistent state
	 */
	@Before
	public void setup() {
		service = new KalahService();
		service.setupGame1();
	}
	
	/**
	 * Test a valid move returns a state of the game rather than an exception.
	 */
	@Test
	public void testPlayGameWithValidMove() {
		Game game = service.playGame(KalahService.GAME_1, "bill", 3);
		assertNotNull(game);
	}
	
	/**
	 * Test invalid game scenario
	 */
	@Test
	public void testPlayGameWithInvalidGameID() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Not a valid game");
		service.playGame("invalidid", "bill", 3);
	}
	
	/**
	 * Test invalid person scenario results in the correct exception and message.
	 */
	@Test
	public void testPlayGameWithInvalidPersonMove() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Not your turn sorry :-)");
		service.playGame(KalahService.GAME_1, "bob", 3);
	}
	
	/**
	 * Test invalid pit selected results in the correct exception and message.
	 */
	@Test
	public void testPlayGameWithInvalidPitNumber() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Not a valid pit number");
		service.playGame(KalahService.GAME_1, "bill", 12);
	}
	
	/**
	 * Test empty pit selected results in the correct exception and message.
	 */
	@Test
	public void testPlayGameWithInvalidPitChosen() {
		Game game = service.findGame(KalahService.GAME_1);
		game.getPlayer1().pickupStonesFromPit(1);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Pit is empty, try another one!!");
		service.playGame(KalahService.GAME_1, "bill", 1);
	}	
	
	/**
	 * Test a game selected that is complete results in the correct exception and message.
	 */
	@Test
	public void testPlayGameWithGameEnded() {
		Game game = service.findGame(KalahService.GAME_1);
		game.getPlayer1().pickupStonesFromPit(1);
		game.getPlayer1().pickupStonesFromPit(2);
		game.getPlayer1().pickupStonesFromPit(3);
		game.getPlayer1().pickupStonesFromPit(4);
		game.getPlayer1().pickupStonesFromPit(5);
		game.getPlayer1().pickupStonesFromPit(6);
		
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Game has been completed, play another?");
		service.playGame(KalahService.GAME_1, "bill", 1);
	}	
}