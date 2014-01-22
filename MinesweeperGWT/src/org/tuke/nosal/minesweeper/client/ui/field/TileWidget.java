package org.tuke.nosal.minesweeper.client.ui.field;

import org.tuke.nosal.minesweeper.client.core.Clue;
import org.tuke.nosal.minesweeper.client.core.Field;
import org.tuke.nosal.minesweeper.client.core.Mine;
import org.tuke.nosal.minesweeper.client.core.Tile;
import org.tuke.nosal.minesweeper.client.core.Tile.State;
import org.tuke.nosal.minesweeper.client.core.statechange.StateChangeObserver;
import org.tuke.nosal.minesweeper.client.resources.MinesweeperResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;

public class TileWidget extends Button implements
		StateChangeObserver<Tile.State> {

	private static final String foregroundColors[] = { "blue", "red", "green",
			"cyan", "magenta", "orange", "plum", "gold" };

	private final int row;
	private final int column;
	private final Field field;
	private String currentStyle;
	private Node currentImage = null;
	private final Node MARK = new Image(MinesweeperResources.IMPL.mark())
			.getElement();
	private final Node MINE = new Image(MinesweeperResources.IMPL.mine())
			.getElement();

	public TileWidget(Field field, int row, int column) {
		super();
		this.field = field;
		this.row = row;
		this.column = column;
		// programmatical setting of style
		this.setSize("20px", "20px");

		// for global styling (minesweeper.css)
		this.addStyleName("tileWidget");

		// styling through css classes and bundles
		this.currentStyle = MinesweeperResources.IMPL.tileComponents().closed();
		this.addStyleName(currentStyle);

		updateTileComponent();

		// register this tileComponent to observe underlying tile
		field.getTile(row, column).registerObserver(this);

		// indicates event types we want to intercept from within this widget's
		// void onBrowserEvent(Event event) method
		sinkEvents(Event.ONCLICK);
		sinkEvents(Event.ONCONTEXTMENU);
	}

	/**
	 * We don't want to provide clicking handling for our TileComponent, the
	 * TileComponent object will handle clicking on its own.
	 */
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		GWT.log("", new Exception("Cannot add ClickHandler "
				+ "to TileComponent"));
		return null;
	}

	/**
	 * Overrides TileComponent's behavior in case of sunk events. Used to handle
	 * open tiles with left button and marking closed tiles with right button.
	 */
	@Override
	public void onBrowserEvent(Event event) {
		switch (event.getTypeInt()) {
		case Event.ONCLICK:
			if (event.getButton() == NativeEvent.BUTTON_LEFT) {
				field.openTile(row, column);
			}
			break;
		case Event.ONCONTEXTMENU:
			if (event.getButton() == NativeEvent.BUTTON_RIGHT) {
				field.markTile(row, column);
				// no default context menu
				event.preventDefault();
			}
			break;
		}
		super.onBrowserEvent(event);
	}

	/**
	 * Observer pattern to make sure tiles are rendered properly even during
	 * automatic opening adjacent tiles.
	 */
	@Override
	public void stateChanged(State event) {
		updateTileComponent();
	}

	/**
	 * Method updates the TileComponent representation according to the
	 * underlying tile's state. It's protected so the parent field component can
	 * update it if it is necessary.
	 */
	protected void updateTileComponent() {
		// reset the button
		if (currentImage != null) {
			getElement().removeChild(currentImage);
			currentImage = null;
		}
		removeStyleName(currentStyle);
		setText("");

		// give it a new style according to tile's state
		Tile tile = field.getTile(row, column);
		switch (tile.getState()) {
		case CLOSED:
			currentStyle = MinesweeperResources.IMPL.tileComponents().closed();
			addStyleName(currentStyle);
			break;

		case MARKED:
			currentStyle = MinesweeperResources.IMPL.tileComponents().marked();
			addStyleName(currentStyle);
			currentImage = MARK;
			getElement().appendChild(currentImage);
			break;

		case OPEN:
			if (tile instanceof Mine) {
				currentStyle = MinesweeperResources.IMPL.tileComponents()
						.openedMine();
				addStyleName(currentStyle);
				currentImage = MINE;
				getElement().appendChild(currentImage);
			} else {
				int value = ((Clue) tile).getValue();
				currentStyle = MinesweeperResources.IMPL.tileComponents()
						.openedClue();
				addStyleName(currentStyle);
				this.setText(Integer.toString(value));

				// low level DOM style setting
				this.getElement().getStyle()
						.setProperty("color", foregroundColors[value % 8]);
			}
			break;

		default:
			GWT.log("", new Exception("Unknown state of tile[" + row + "]["
					+ column + "]"));
		}
	}
}
