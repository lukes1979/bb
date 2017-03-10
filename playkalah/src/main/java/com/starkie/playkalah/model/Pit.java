package com.starkie.playkalah.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Model object for a pit
 * @author luke.starkie
 */
public class Pit {

	/**
	 * The number of stones currently held in the pit
	 */
	private int numberOfStonesInPit;
	
	public Pit(int startingStoneCount) {
		numberOfStonesInPit = startingStoneCount;
	}

	public int getNumberOfStonesInPit() {
		return numberOfStonesInPit;
	}
	
	public void addStone() {
		numberOfStonesInPit++;
	}

	/**
	 * Returns the number of stones and clears the pit.
	 * @return
	 */
	public int pickupStones() {
		int originalNumberOfStonesInPit = numberOfStonesInPit;
		this.numberOfStonesInPit = 0;
		return originalNumberOfStonesInPit;
	}

	@Override
	public String toString() {
		return Integer.toString(numberOfStonesInPit);
	}
	
	@JsonIgnore
	public boolean isEmpty() {
		return numberOfStonesInPit == 0;
	}
}