package org.tuke.nosal.minesweeper.client;

import org.tuke.nosal.minesweeper.client.resources.MinesweeperResources;
import org.tuke.nosal.minesweeper.client.ui.MainPage;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Minesweeper implements EntryPoint {

	// private UserInterface userInterface;

	@Override
	public void onModuleLoad() {

		MinesweeperResources.IMPL.tileComponents().ensureInjected();

		RootLayoutPanel.get().add(new MainPage());

	}

}
