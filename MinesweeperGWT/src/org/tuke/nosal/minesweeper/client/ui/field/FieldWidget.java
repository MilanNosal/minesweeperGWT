package org.tuke.nosal.minesweeper.client.ui.field;

import org.tuke.nosal.minesweeper.client.core.Field;
import org.tuke.nosal.minesweeper.client.core.GameState;
import org.tuke.nosal.minesweeper.client.core.UserInterface;
import org.tuke.nosal.minesweeper.client.core.statechange.StateChangeObserver;
import org.tuke.nosal.minesweeper.client.ui.MainPage;
import org.tuke.nosal.minesweeper.shared.BestTimesService;
import org.tuke.nosal.minesweeper.shared.BestTimesServiceAsync;
import org.tuke.nosal.minesweeper.shared.SimpleNameValidator;
import org.tuke.nosal.minesweeper.shared.dto.PlayerTime;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * I believe using UiBinder the code would be nicer, but to show how to create
 * own widget without UiBinder.
 * 
 * @author Milan
 * 
 */
public class FieldWidget extends Composite implements UserInterface,
		StateChangeObserver<GameState> {

	/**
	 * Current game field.
	 */
	private Field gameField;

	/**
	 * Representation for the current field, it is going to be a Grid with
	 * TileWidgets for tiles.
	 */
	private final Grid fieldRepresentation = new Grid(0, 0);

	/**
	 * Top bar showing mines left and current time.
	 */
	private final TopFieldWidget topComponent = new TopFieldWidget();

	private final NameDialog nameDialogBox = new NameDialog();

	private final BestTimesServiceAsync bestTimes = GWT
			.create(BestTimesService.class);

	/**
	 * Sets up the composite, but so far without a concrete field.
	 */
	public FieldWidget() {
		super();

		fieldRepresentation.addStyleName("fieldCompositeCore");

		VerticalPanel vp = new VerticalPanel();
		vp.add(topComponent);
		vp.setCellHeight(topComponent, "30px");

		vp.add(fieldRepresentation);
		vp.addStyleName("fieldComposite");

		ScrollPanel scp = new ScrollPanel(vp);
		scp.setSize("100%", "100%");

		initWidget(scp);

		final SimpleNameValidator validator = new SimpleNameValidator();

		nameDialogBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String name = nameDialogBox.getText();
				if (validator.validateName(name)) {
					nameDialogBox.hide();
					registerGameTime(name);
				} else {
					Window.alert("Your name is not valid!");
				}
			}

		});
	}

	public FieldWidget(Field gameField) {
		this();
		if (gameField == null)
			throw new IllegalStateException("Field cannot be null.");
		newGameStarted(gameField);
	}

	/**
	 * Builds the component view. Only called when a new field is set, so we can
	 * be sure that the rebuild is necessary.
	 */
	private void buildFieldRepresentation() {
		int rowCount = gameField.getRowCount();
		int columnCount = gameField.getColumnCount();

		// clean-up in case field component is reused
		fieldRepresentation.clear();
		fieldRepresentation.resize(rowCount, columnCount);

		for (int currRow = 0; currRow < rowCount; currRow++) {
			for (int currColumn = 0; currColumn < columnCount; currColumn++) {
				fieldRepresentation.setWidget(currRow, currColumn,
						new TileWidget(gameField, currRow, currColumn));
			}
		}
	}

	/**
	 * Starts a new game with the given field.
	 */
	@Override
	public void newGameStarted(Field field) {
		gameField = field;
		gameField.registerObserver(this);
		topComponent.newGameStarted(field);
		buildFieldRepresentation();
	}

	/**
	 * Explicit tiles representation update, but hopefully no needed since
	 * TileWidgets observe state changes of their corresponding Tiles.
	 */
	@Override
	public void update() {
		if (gameField == null)
			throw new IllegalStateException(
					"Update method called when the widget is not initialized.");
		int rowCount = gameField.getRowCount();
		int columnCount = gameField.getColumnCount();
		for (int currRow = 0; currRow < rowCount; currRow++) {
			for (int currColumn = 0; currColumn < columnCount; currColumn++) {
				((TileWidget) fieldRepresentation
						.getWidget(currRow, currColumn)).updateTileComponent();
			}
		}
	}

	/**
	 * We observe the field to catch a victory or a game loss and act on it
	 * accordingly.
	 */
	@Override
	public void stateChanged(GameState event) {
		if (event == GameState.SOLVED) {
			this.nameDialogBox.show();
		} else if (event == GameState.FAILED) {
			Window.alert("Oops! I am sorry, you have lost the game!");
		}
	}

	/**
	 * Registers a new game victory to server through GWT RPC service.
	 * @param text
	 */
	private void registerGameTime(String text) {
		PlayerTime pt = new PlayerTime(text, gameField.getGameDuration());
		bestTimes.registerBestTime(pt, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error during registering a new best time.\n" + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(Void result) {
				// A workaround to show best times using history implementation
				History.newItem(MainPage.BEST_TIMES_TOKEN);
				History.fireCurrentHistoryState();
			}
			
		});
	}
}
