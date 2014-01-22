package org.tuke.nosal.minesweeper.shared;

/**
 * A simple exception to see Exception handling in GWT. java.lang.Exception is
 * supported by GWT and therefore we don't need to specify it is serializable.
 * But we still have to keep zero-arg constructor rule.
 * 
 * @author Milan
 * 
 */
public class BestTimesException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Zero-arg constructor to be GWT-compatible.
	 */
	public BestTimesException() {

	}

	/**
	 * And one for our convenience.
	 * 
	 * @param cause
	 */
	public BestTimesException(String cause) {
		super(cause);
	}
}
