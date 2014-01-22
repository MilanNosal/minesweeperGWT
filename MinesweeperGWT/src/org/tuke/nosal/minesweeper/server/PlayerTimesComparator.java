package org.tuke.nosal.minesweeper.server;

import java.util.Comparator;

import org.tuke.nosal.minesweeper.shared.dto.PlayerTime;

/**
 * Could be implemented also as an Comparable implementation upon PlayerTime,
 * but this would have to use GWT compatible types. Here we are in server side
 * code only so we can use whatever we want (as long as we put it on the
 * classpath :) ).
 * 
 * @author Milan
 * 
 */
public class PlayerTimesComparator implements Comparator<PlayerTime> {

	@Override
	public int compare(PlayerTime time1, PlayerTime time2) {
		if (time1.getTime() - time2.getTime() == 0) {
			return time1.getName().compareTo(time2.getName());
		} else {
			return time1.getTime() - time2.getTime();
		}
	}

}
