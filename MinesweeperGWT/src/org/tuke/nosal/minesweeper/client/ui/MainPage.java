package org.tuke.nosal.minesweeper.client.ui;

import org.tuke.nosal.minesweeper.client.core.Field;
import org.tuke.nosal.minesweeper.client.resources.MinesweeperResources;
import org.tuke.nosal.minesweeper.client.ui.field.FieldWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.ResizeComposite;

public class MainPage extends ResizeComposite implements
		ValueChangeHandler<String> {

	public static final String SUPER_EASY_GAME_TOKEN = "superEasy";
	public static final String BEGINNER_GAME_TOKEN = "beginner";
	public static final String INTERMEDIATE_GAME_TOKEN = "intermediate";
	public static final String EXPERT_GAME_TOKEN = "expert";
	public static final String BEST_TIMES_TOKEN = "bestTimes";
	public static final String ABOUT_TOKEN = "about";
	public static final String HELP_TOKEN = "help";

	private static MainPageUiBinder uiBinder = GWT
			.create(MainPageUiBinder.class);

	interface MainPageUiBinder extends UiBinder<DockLayoutPanel, MainPage> {
	}

	@UiField()
	DeckLayoutPanel contentsPanel;

	@UiField()
	MenuItem beginnerGameMenuItem;

	@UiField()
	MenuItem intermediateGameMenuItem;

	@UiField()
	MenuItem expertGameMenuItem;

	@UiField()
	MenuItem aboutMenuItem;

	@UiField()
	MenuItem helpMenuItem;

	@UiField()
	MenuItem bestTimesMenuItem;

	@UiField()
	MenuItem superEasyMenuItem;

	@UiField()
	FieldWidget fieldRepr;

	@UiField
	HTML aboutPanel;

	@UiField
	HTML helpPanel;
	
	@UiField
	HTML homePagePanel;

	@UiField
	BestTimesPanel bestTimesPanel;

	public MainPage() {
		initWidget(uiBinder.createAndBindUi(this));

		// only after the UiBinder dependency injection
		superEasyMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				showGame(4, 4, 2);
				History.newItem(SUPER_EASY_GAME_TOKEN); 
			}
		});

		beginnerGameMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				showGame(8, 8, 10);
				History.newItem(BEGINNER_GAME_TOKEN);
			}
		});

		intermediateGameMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				showGame(16, 16, 40);
				History.newItem(INTERMEDIATE_GAME_TOKEN);
			}
		});

		expertGameMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				showGame(24, 24, 99);
				History.newItem(EXPERT_GAME_TOKEN);
			}
		});

		aboutMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				showAbout();
				History.newItem(ABOUT_TOKEN);
			}
		});

		helpMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				showHelp();
				History.newItem(HELP_TOKEN);
			}
		});

		bestTimesMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				showBestTimes();
				History.newItem(BEST_TIMES_TOKEN);
			}
		});

		aboutPanel.setHTML(MinesweeperResources.IMPL.aboutPage().getText());
		helpPanel.setHTML(MinesweeperResources.IMPL.helpPage().getText());
		
		setUpHistoryManagement();
	}

	private void showGame(int rows, int columns, int mines) {
		fieldRepr.newGameStarted(new Field(rows, columns, mines));
		contentsPanel.showWidget(fieldRepr);
	}

	private void showAbout() {
		contentsPanel.showWidget(aboutPanel);
	}

	private void showHelp() {
		contentsPanel.showWidget(helpPanel);
	}

	private void showBestTimes() {
		contentsPanel.showWidget(bestTimesPanel);
		bestTimesPanel.update();
	}

	private void showHomePage() {
		contentsPanel.showWidget(homePagePanel);
	}

	public void setUpHistoryManagement() {
		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();
		
		Window.addWindowClosingHandler(new ClosingHandler(){
			public void onWindowClosing(ClosingEvent event) {
				event.setMessage("Proceeding will exit the application. Are you sure?");
			}
		});
	}

	/**
	 * This method sets the current view according to the history token that is
	 * provided.
	 */
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// prepares token
		String token = event.getValue();
		if (event.getValue() != null)
			token = event.getValue().trim();
			
		if (SUPER_EASY_GAME_TOKEN.equals(token)) {
			showGame(4, 4, 2);
		} else if (BEGINNER_GAME_TOKEN.equals(token)) {
			showGame(8, 8, 10);
		} else if(INTERMEDIATE_GAME_TOKEN.equals(token)) {
			showGame(16, 16, 40);
		} else if (EXPERT_GAME_TOKEN.equals(token)) {
			showGame(24, 24, 99);
		} else if (ABOUT_TOKEN.equals(token)) {
			showAbout();
		} else if (HELP_TOKEN.equals(token)) {
			showHelp();
		} else if (BEST_TIMES_TOKEN.equals(token)) {
			showBestTimes();
		} else {
			// if the token is not recognized, the home will be shown
			showHomePage();
		}
	}
}
