package org.tuke.nosal.minesweeper.shared;

import java.util.ArrayList;

import org.tuke.nosal.minesweeper.shared.dto.PlayerTime;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous interface for our BestTimesService. Each async method has void
 * as return type and has one new argument that is AsyncCallback parametrized to
 * the return type of synchronous method. Notice
 * AsyncCallback<ArrayList<PlayerTime>> for getBestTimes method with
 * ArrayList<PlayerTime> return type and java.lang.Void parameter of
 * AsyncCallback for registerBestTimes.
 * 
 * @author Milan
 * 
 */
public interface BestTimesServiceAsync {
	void getBestTimes(AsyncCallback<ArrayList<PlayerTime>> callback);

	void registerBestTime(PlayerTime pt, AsyncCallback<Void> callback);
}
