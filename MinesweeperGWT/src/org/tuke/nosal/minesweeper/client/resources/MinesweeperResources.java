package org.tuke.nosal.minesweeper.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface MinesweeperResources extends ClientBundle {

	MinesweeperResources IMPL = GWT.create(MinesweeperResources.class);

	@Source("logo.gif")
	ImageResource logo();

	@Source("mark.gif")
	ImageResource mark();

	@Source("mine.gif")
	ImageResource mine();

	@Source("smile.gif")
	ImageResource smile();

	/**
	 * We should be careful when working with text to be safe from possible
	 * attacks.
	 * 
	 * @return
	 */
	@Source("about.html")
	TextResource aboutPage();
	
	@Source("help.html")
	TextResource helpPage();

	@Source("tileComponents.css")
	TileComponents tileComponents();

	public interface TileComponents extends CssResource {

		String openedMine();

		String marked();

		String openedClue();

		String closed();

	}

}
