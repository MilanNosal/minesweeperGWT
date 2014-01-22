package org.tuke.nosal.minesweeper.client.core.statechange;

import java.util.HashSet;
import java.util.Set;

public class StateChangeSubject<T extends Enum> {

	private Set<StateChangeObserver<T>> observers = new HashSet<StateChangeObserver<T>>();
	
	public void stateChanged(T newState) {
    	for(StateChangeObserver<T> observer : observers) {
    		observer.stateChanged(newState);
    	}
    }

	public void registerObserver(StateChangeObserver<T> observer) {
		observers.add(observer);
	}

	public void unregisterObserver(StateChangeObserver<T> observer) {
		observers.remove(observer);
	}
}
