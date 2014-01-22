package org.tuke.nosal.minesweeper.client.core;


public interface UserInterface {

	/**
	 * Starts a new game with a given field.
	 * @param field
	 */
	public void newGameStarted(Field field);
	
	/**
	 * Updates the game field visualization.
	 */
	public void update();
}
