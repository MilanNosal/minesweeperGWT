package org.tuke.nosal.minesweeper.client.core;

import org.tuke.nosal.minesweeper.client.core.Tile.State;
import org.tuke.nosal.minesweeper.client.core.statechange.StateChangeSubject;

import com.google.gwt.core.client.Duration;
import com.google.gwt.user.client.Random;

/**
 * Field represents playing field and game logic.
 */
public class Field extends StateChangeSubject<GameState> {
	/** Playing field tiles. */
	private final Tile[][] tiles;

	/** Field row count. Rows are indexed from 0 to (rowCount - 1). */
	private final int rowCount;

	/** Column count. Columns are indexed from 0 to (columnCount - 1). */
	private final int columnCount;

	/** Mine count. */
	private final int mineCount;

	/** Game state. */
	private GameState state = GameState.PLAYING;

	/** Duration from the start of the game. */
	private Duration duration = null;

	/** Set only after the game is over. */
	private int gameTime = 0;

	/**
	 * Constructor.
	 * 
	 * @param rowCount
	 *            row count
	 * @param columnCount
	 *            column count
	 * @param mineCount
	 *            mine count
	 */
	public Field(int rowCount, int columnCount, int mineCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.mineCount = mineCount;
		tiles = new Tile[rowCount][columnCount];
		generate();
	}

	public GameState getState() {
		return state;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public int getMineCount() {
		return mineCount;
	}

	public Tile getTile(int row, int column) {
		return this.tiles[row][column];
	}

	/**
	 * Returns time from starting the game. Game is started when player open
	 * firts tile.
	 * 
	 * @return time in seconds.
	 */
	public int getGameDuration() {
		if (duration != null) {
			return duration.elapsedMillis() / 1000;
		} else {
			return gameTime;
		}
	}

	/**
	 * Opens tile at specified indeces.
	 * 
	 * @param row
	 *            row number
	 * @param column
	 *            column number
	 */
	public void openTile(int row, int column) {
		final Tile tile = tiles[row][column];
		if (tile.getState() == Tile.State.CLOSED && state == GameState.PLAYING) {
			if (duration == null) {
				duration = new Duration();
			}
			tile.setState(Tile.State.OPEN);
			if (tile instanceof Clue && ((Clue) tile).getValue() == 0) {
				openAdjacentTiles(row, column);
			} else if (tile instanceof Mine) {
				this.gameTime = duration.elapsedMillis() / 1000;
				duration = null;
				state = GameState.FAILED;
				openAllMines();
				this.stateChanged(state);
			}
		}
		
		if (isSolved() && state == GameState.PLAYING) {
			this.gameTime = duration.elapsedMillis() / 1000;
			duration = null;
			state = GameState.SOLVED;
			this.stateChanged(state);
		}
	}

	/**
	 * Recursively opens adjacent tiles using void openTile(int,int).
	 * 
	 * @param row
	 * @param column
	 */
	private void openAdjacentTiles(int row, int column) {
		for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
			int actRow = row + rowOffset;
			if (actRow >= 0 && actRow < rowCount) {
				for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
					int actColumn = column + columnOffset;
					if (actColumn >= 0 && actColumn < columnCount) {
						openTile(actRow, actColumn);
					}
				}
			}
		}
	}

	/**
	 * Marks tile at specified indeces.
	 * 
	 * @param row
	 *            row number
	 * @param column
	 *            column number
	 */
	public void markTile(int row, int column) {
		final Tile tile = tiles[row][column];
		if (tile.getState() == Tile.State.CLOSED && state == GameState.PLAYING) {
			tile.setState(Tile.State.MARKED);
		} else if (tile.getState() == Tile.State.MARKED && state == GameState.PLAYING) {
			tile.setState(Tile.State.CLOSED);
		}
	}

	/**
	 * Generates playing field.
	 */
	private void generate() {
		generateMines();
		fillWithClues();
	}

	/**
	 * Method fills the rest of the field with clues. Field should contain mines
	 * and empty tiles (tile[i][j] == null).
	 */
	private void fillWithClues() {
		for (int rowNum = 0; rowNum < this.rowCount; rowNum++) {
			for (int columnNum = 0; columnNum < this.columnCount; columnNum++) {
				if (tiles[rowNum][columnNum] == null) {
					int adjacentMinesCount = this.countAdjacentMines(rowNum,
							columnNum);
					tiles[rowNum][columnNum] = new Clue(adjacentMinesCount);
				}
			}
		}
	}

	/**
	 * Method generates mines to random tiles in the game field. Expects that
	 * mineCount <= rowCount * columnCount and that tiles are empty (tile[i][j]
	 * == null for 0 < i < rowCount and 0 < j < columnCount).
	 */
	private void generateMines() {
		int actMines = 0;
		while (actMines < this.mineCount) {
			int row = Random.nextInt(this.rowCount);
			int column = Random.nextInt(this.columnCount);
			if (this.tiles[row][column] == null) {
				this.tiles[row][column] = new Mine();
				actMines++;
			}
		}
	}

	/**
	 * Returns true if game is solved, false otherwise.
	 * 
	 * @return true if game is solved, false otherwise
	 */
	private boolean isSolved() {
		return (this.rowCount * this.columnCount)
				- this.getNumberOf(Tile.State.OPEN) == this.mineCount;
	}

	/**
	 * Counts how many unmarked mines are there left.
	 * 
	 * @return
	 */
	public int getNumberOfMines() {
		return this.mineCount - getNumberOf(State.MARKED);
	}

	/**
	 * Counts tiles in the game field that are in a given state.
	 * 
	 * @param state
	 *            in which the tiles should be
	 * @return count of the tiles in a given state
	 */
	private int getNumberOf(Tile.State state) {
		int count = 0;
		for (int rowNum = 0; rowNum < this.rowCount; rowNum++) {
			for (int columnNum = 0; columnNum < this.columnCount; columnNum++) {
				if (tiles[rowNum][columnNum].getState() == state) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Opens all mines when the game is lost.
	 */
	private void openAllMines() {
		for (int rowNum = 0; rowNum < this.rowCount; rowNum++) {
			for (int columnNum = 0; columnNum < this.columnCount; columnNum++) {
				if (tiles[rowNum][columnNum] instanceof Mine) {
					tiles[rowNum][columnNum].setState(Tile.State.OPEN);
				}
			}
		}
	}

	/**
	 * Returns number of adjacent mines for a tile at specified position in the
	 * field.
	 * 
	 * @param row
	 *            row number.
	 * @param column
	 *            column number.
	 * @return number of adjacent mines.
	 */
	private int countAdjacentMines(int row, int column) {
		int count = 0;

		for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
			int actRow = row + rowOffset;
			if (actRow >= 0 && actRow < rowCount) {
				for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
					int actColumn = column + columnOffset;
					if (actColumn >= 0 && actColumn < columnCount) {
						if (tiles[actRow][actColumn] instanceof Mine) {
							count++;
						}
					}
				}
			}
		}

		return count;
	}
}
