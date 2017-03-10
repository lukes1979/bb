package com.starkie.playkalah.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Game model class
 * @author luke.starkie
 */
public class GameTest {
	
	private Game game;
	
	/**
	 * Set up a new game in a consistent state
	 */
	@Before
	public void setup() {
		game = new Game("bill", "bob", "1");
	}
	
	/**
	 * Test a simple move with a new game (pit 3 selected).
	 * 		In play user:
	 * 			pit 3 empty
	 * 			pits 4,5,6 incremented
	 * 			pits 1,2 unchanged
	 * 			kalah incremented
	 * 		Non playing user:
	 * 			pits 1,2 incremented
	 * 			pits 3,4,5,6 unchanged
	 * 			kalah unchanged
	 */
	@Test
	public void testPlayGameWithSimpleMove() {
		game.playPit("bill", 3);
		
		assertEquals(6, game.getPlayer1().getPit(1).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer1().getPit(2).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer1().getPit(3).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer1().getPit(4).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer1().getPit(5).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer1().getPit(6).getNumberOfStonesInPit());
		assertEquals(1, game.getPlayer1().getNumberOfStonesInKalah());

		assertEquals(7, game.getPlayer2().getPit(1).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer2().getPit(2).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer2().getPit(3).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer2().getPit(4).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer2().getPit(5).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer2().getPit(6).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer2().getNumberOfStonesInKalah());
		
		assertEquals("bob", game.getNextPlayerToMove());
	}
	
	/**
	 * Test when the number of stones wraps around the opponents kalah is ignored and the original users pits are used
	 */
	@Test
	public void testPlayGameWithWrapAroundIgnoreOpponentsKalah() {
		game.getPlayer1().getPit(6).addStone();
		game.getPlayer1().getPit(6).addStone();
		
		game.playPit("bill", 6);
		
		assertEquals(7, game.getPlayer1().getPit(1).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer1().getPit(2).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer1().getPit(3).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer1().getPit(4).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer1().getPit(5).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer1().getPit(6).getNumberOfStonesInPit());
		assertEquals(1, game.getPlayer1().getNumberOfStonesInKalah());

		assertEquals(7, game.getPlayer2().getPit(1).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer2().getPit(2).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer2().getPit(3).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer2().getPit(4).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer2().getPit(5).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer2().getPit(6).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer2().getNumberOfStonesInKalah());
		
		assertEquals("bob", game.getNextPlayerToMove());
	}	
	
	/**
	 * Test when the last stone lands on the kalah the same user is still in play
	 */
	@Test
	public void testPlayGameAnotherGoAfterLandingOnKalah() {
		
		game.playPit("bill", 1);
		
		assertEquals(0, game.getPlayer1().getPit(1).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer1().getPit(2).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer1().getPit(3).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer1().getPit(4).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer1().getPit(5).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer1().getPit(6).getNumberOfStonesInPit());
		assertEquals(1, game.getPlayer1().getNumberOfStonesInKalah());

		assertEquals(6, game.getPlayer2().getPit(1).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer2().getPit(2).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer2().getPit(3).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer2().getPit(4).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer2().getPit(5).getNumberOfStonesInPit());
		assertEquals(6, game.getPlayer2().getPit(6).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer2().getNumberOfStonesInKalah());
		
		assertEquals("bill", game.getNextPlayerToMove());
	}
	
	/**
	 * Test when the no valid moves remain then the stones are moved to kalah and the game is completed
	 */
	@Test
	public void testPlayGameWithEndMoveKalah() {
		
		assertFalse(game.isGameCompleted());
		
		game.getPlayer1().getPit(1).pickupStones();
		game.getPlayer1().getPit(1).addStone();
		
		game.getPlayer2().addStoneToKalah();
		game.getPlayer2().getPit(1).pickupStones();
		game.getPlayer2().getPit(2).pickupStones();
		game.getPlayer2().getPit(3).pickupStones();
		game.getPlayer2().getPit(4).pickupStones();
		game.getPlayer2().getPit(5).pickupStones();
		game.getPlayer2().getPit(6).pickupStones();

		game.playPit("bill", 1);
		
		assertEquals(0, game.getPlayer1().getPit(1).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer1().getPit(2).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer1().getPit(3).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer1().getPit(4).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer1().getPit(5).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer1().getPit(6).getNumberOfStonesInPit());
		assertEquals(31, game.getPlayer1().getNumberOfStonesInKalah());

		assertEquals(0, game.getPlayer2().getPit(1).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer2().getPit(2).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer2().getPit(3).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer2().getPit(4).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer2().getPit(5).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer2().getPit(6).getNumberOfStonesInPit());
		assertEquals(1, game.getPlayer2().getNumberOfStonesInKalah());
		assertTrue(game.isGameCompleted());
	}
	
	/**
	 * Test when the last stone lands on the kalah the same user is still in play.
	 */
	@Test
	public void testPlayGameLandOnEmptyPitAndCaptureOpponents() {
		
		game.getPlayer1().getPit(6).addStone();
		game.getPlayer1().getPit(6).addStone();
		game.getPlayer1().getPit(1).pickupStones();
		
		game.playPit("bill", 6);
		assertEquals(0, game.getPlayer1().getPit(1).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer1().getPit(6).getNumberOfStonesInPit());
		assertEquals(9, game.getPlayer1().getNumberOfStonesInKalah());

		assertEquals(7, game.getPlayer2().getPit(1).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer2().getPit(2).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer2().getPit(3).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer2().getPit(4).getNumberOfStonesInPit());
		assertEquals(7, game.getPlayer2().getPit(5).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer2().getPit(6).getNumberOfStonesInPit());
		assertEquals(0, game.getPlayer2().getNumberOfStonesInKalah());
	}
}