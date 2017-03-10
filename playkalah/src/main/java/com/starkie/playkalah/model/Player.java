package com.starkie.playkalah.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Model object for a player and their game state.
 * @author luke.starkie
 */
public class Player {
	
	private static final Logger logger = LoggerFactory.getLogger(Player.class);

	/**
	 * The name of the player
	 */
	private String name;
	
	/**
	 * The pits the player owns.
	 */
	private List<Pit> pits;
	
	/**
	 * The number of stones in the kalah
	 */
	private int numberOfStonesInKalah = 0;
	
	/**
	 * Creates a player via a name and game number of pits and starting stones
	 * @param name the name of the player
	 * @param startingPits the starting number of pits
	 * @param startingStones the starting number of stones in the pits
	 */
	public Player(String name, int startingPits, int startingStones) {
		this.name = name;
		this.pits = new ArrayList<Pit>(startingPits);
		for (int i = 0; i < startingPits; i++) {
			this.pits.add(new Pit(startingStones));
		}
	}
	
	public String getName() {
		return name;
	}

	public List<Pit> getPits() {
		return pits;
	}
	
	public int getNumberOfStonesInKalah() {
		return numberOfStonesInKalah;
	}
	
	@JsonIgnore
	public Pit getPit(int pitNumber) {
		return pits.get(pitNumber-1);
	}
	
	/**
	 * Add a stone to the specified pit
	 * @param pitNumber pit to use
	 */
	public void addStoneToPit(int pitNumber) {
		logger.info("Addng stone to pit {} for user {}", pitNumber, name);
		getPit(pitNumber).addStone();
	}
	
	/**
	 * Pickup the stones from the pit specified (clears the stones)
	 * @param pitNumber pit to use
	 * @return
	 */
	public int pickupStonesFromPit(int pitNumber) {
		logger.info("Picking up stones from pit {} for user {}", pitNumber, name);
		return getPit(pitNumber).pickupStones();
	}
	
	@JsonIgnore
	public boolean isKalehEmpty() {
		return numberOfStonesInKalah == 0;
	}

	/**
	 * Add a stone to the kalah
	 */
	public void addStoneToKalah() {
		logger.info("Adding stone to kalah for user {}", name);
		this.numberOfStonesInKalah++;
	}

	@Override
	public String toString() {
		return "[name=" + name + ", pits=" + pits + ", kalah=" + numberOfStonesInKalah + "]";
	}

	/**
	 * Check if the players pits are empty of stones
	 * @return
	 */
	@JsonIgnore
	public boolean isPitsEmpty() {
		for (Pit pit : pits) {
			if (!pit.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Moves the all the players pit stones to the kalah
	 */
	public void movePitStonesToKalah() {
		logger.info("Moving pit stones to kalah for user {}", name);
		for (Pit pit : pits) {
			if (!pit.isEmpty()) {
				int stonesInPit = pit.pickupStones();
				numberOfStonesInKalah += stonesInPit;
			}
		}
	}

	@JsonIgnore
	public int getOppositePitStones(int pitNumber) {
		switch (pitNumber) {
		case 1:
			return pits.get(5).pickupStones();
		case 2:
			return pits.get(4).pickupStones();
		case 3:
			return pits.get(3).pickupStones();
		case 4:
			return pits.get(2).pickupStones();
		case 5:
			return pits.get(1).pickupStones();
		case 6:
			return pits.get(0).pickupStones();	
		}
		throw new IllegalStateException("Invalid pit for selecting stones");
	}

	/**
	 * Add the opponents stones passed in to the kalah
	 * @param opponentStones opponents stones
	 */
	public void addOpponentsStonesToKalah(int opponentStones) {
		logger.info("Adding {} opponent stone to kalah for user {}", opponentStones, name);
		numberOfStonesInKalah += opponentStones;
	}
}