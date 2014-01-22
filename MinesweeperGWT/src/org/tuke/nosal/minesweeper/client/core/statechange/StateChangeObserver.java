package org.tuke.nosal.minesweeper.client.core.statechange;

public interface StateChangeObserver<T extends Enum> {
	
	public void stateChanged(T event);
	
}
