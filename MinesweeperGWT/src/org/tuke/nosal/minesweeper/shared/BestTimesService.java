package org.tuke.nosal.minesweeper.shared;

import java.util.ArrayList;

import org.tuke.nosal.minesweeper.shared.dto.PlayerTime;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service interface. It needs to extend RemoteService (or its descendant).
 * \@RemoteServiceRelativePath annotation defines the relative path to the
 * servlet implementing this service. Thanks to this annotation client side of
 * code is simpler.
 * 
 * @author Milan
 * 
 */
@RemoteServiceRelativePath("bestTimesService")
public interface BestTimesService extends RemoteService {
	public void registerBestTime(PlayerTime pt) throws BestTimesException;

	/**
	 * In GWT RPC methods be always specific, rather ArrayList then List.
	 * 
	 * @return
	 */
	public ArrayList<PlayerTime> getBestTimes();
}
