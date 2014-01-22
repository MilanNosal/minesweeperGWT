package org.tuke.nosal.minesweeper.server;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;

import org.tuke.nosal.minesweeper.shared.BestTimesException;
import org.tuke.nosal.minesweeper.shared.BestTimesService;
import org.tuke.nosal.minesweeper.shared.SimpleNameValidator;
import org.tuke.nosal.minesweeper.shared.dto.PlayerTime;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class BestTimesServiceImpl extends RemoteServiceServlet implements
		BestTimesService {

	private ArrayList<PlayerTime> bestTimes = new ArrayList<>();
	private SimpleNameValidator validator = new SimpleNameValidator();
	private PlayerTimesComparator comparator = new PlayerTimesComparator();

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		bestTimes.add(new PlayerTime("Milan", 1));
		bestTimes.add(new PlayerTime("Milanko", 5));
		bestTimes.add(new PlayerTime("Milangf",7));
		bestTimes.add(new PlayerTime("Milanee", 1));
		bestTimes.add(new PlayerTime("Milanad", 5));
		bestTimes.add(new PlayerTime("Milanffds", 1));
	}

	@Override
	public void registerBestTime(PlayerTime pt) throws BestTimesException {
		if (validator.validateName(pt.getName())) {
			// test only here to show handling events
			if (pt.getName().equals("Chuck Norris") && pt.getTime() > 0) {
				throw new BestTimesException(
						"Chuck Norris would be faster. You must be an imposter!");
			}
			synchronized (bestTimes) {
				Collections.sort(bestTimes, comparator);
				bestTimes.add(pt);
			}
		} else {
			throw new BestTimesException(
					"Player's name in the provided PlayerTime is not valid.");
		}
	}

	@Override
	public ArrayList<PlayerTime> getBestTimes() {
		synchronized (bestTimes) {
			int length = bestTimes.size();
			// we will return 10 best players (or less if there are not enough
			// submitted times)
			return new ArrayList<>(bestTimes.subList(0, (length > 9) ? 9
					: length));
		}
	}

}
