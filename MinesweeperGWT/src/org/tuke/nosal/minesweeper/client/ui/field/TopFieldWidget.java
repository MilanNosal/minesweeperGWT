package org.tuke.nosal.minesweeper.client.ui.field;

import org.tuke.nosal.minesweeper.client.core.Field;
import org.tuke.nosal.minesweeper.client.core.Tile;
import org.tuke.nosal.minesweeper.client.core.Tile.State;
import org.tuke.nosal.minesweeper.client.core.statechange.StateChangeObserver;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TopFieldWidget extends Composite implements
		StateChangeObserver<Tile.State> {

	private static TopFieldComponentUiBinder uiBinder = GWT
			.create(TopFieldComponentUiBinder.class);

	interface TopFieldComponentUiBinder extends
			UiBinder<Widget, TopFieldWidget> {
	}

	public TopFieldWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Label mineCountingLabel;

	@UiField
	Label timerLabel;

	private Field currentField;

	private Timer timer;

	/**
	 * Sets current game field and refreshes labels.
	 * 
	 * @param field
	 */
	public void newGameStarted(Field field) {
		currentField = field;
		mineCountingLabel.setText(Integer.toString(currentField
				.getNumberOfMines()));
		int rows = field.getRowCount();
		int columns = field.getColumnCount();
		for (int currentRow = 0; currentRow < rows; currentRow++) {
			for (int currentColumn = 0; currentColumn < columns; currentColumn++) {
				field.getTile(currentRow, currentColumn).registerObserver(this);
			}
		}
		timerLabel.setText(Integer.toString(currentField.getGameDuration()));

		if (timer == null) {
			// One timer is enough for one TopComponent, only thing that
			// we have to keep consistent is that currentField has to be set,
			// that's the reason why it is created here instead of constructor
			timer = new Timer() {
				public void run() {
					timerLabel.setText(Integer.toString(currentField
							.getGameDuration()));
				}
			};
			timer.scheduleRepeating(1000);
		}
	}

	/**
	 * Refreshes mine count when a tile is marked or unmarked.
	 */
	@Override
	public void stateChanged(State event) {
		if (event == State.MARKED || event == State.CLOSED) {
			mineCountingLabel.setText(Integer.toString(currentField
					.getNumberOfMines()));
		}
	}
}
