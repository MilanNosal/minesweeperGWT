package org.tuke.nosal.minesweeper.client.ui;

import java.util.ArrayList;

import org.tuke.nosal.minesweeper.shared.BestTimesService;
import org.tuke.nosal.minesweeper.shared.BestTimesServiceAsync;
import org.tuke.nosal.minesweeper.shared.dto.PlayerTime;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

public class BestTimesPanel extends Composite {

	private final BestTimesServiceAsync bestTimes = GWT
			.create(BestTimesService.class);

	private final Grid grid = new Grid(11, 3);

	public BestTimesPanel() {
		initWidget(grid);
		this.addStyleName("bestTimesComposite");
		for (int column = 0; column < 3; column++) {
			grid.getCellFormatter().addStyleName(0, column, "bestTimesHeader");
		}
		for (int row = 1; row < 11; row++) {
			for (int column = 0; column < 3; column++) {
				grid.getCellFormatter().addStyleName(row, column,
						"bestTimesCell");
			}
		}
	}

	public void update() {
		grid.clear();
		bestTimes.getBestTimes(new AsyncCallback<ArrayList<PlayerTime>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Encountered unexpected error when pulling best times.\n"
						+ caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<PlayerTime> result) {
				grid.setWidget(0, 0, new Label("#"));
				grid.setWidget(0, 1, new Label("Player Name"));
				grid.setWidget(0, 2, new Label("Time"));
				for (int i = 1; i < 11; i++) {
					grid.setWidget(i, 0, new Label(i + "."));
					if (i <= result.size()) {
						grid.setWidget(i, 1, new Label(result.get(i - 1)
								.getName()));
						grid.setWidget(
								i,
								2,
								new Label(Integer.toString(result.get(i - 1)
										.getTime())));
					} else {
						grid.setWidget(i, 1, new Label("-"));
						grid.setWidget(i, 2, new Label("-"));
					}
				}
			}

		});
	}

}
