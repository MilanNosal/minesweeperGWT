package org.tuke.nosal.minesweeper.shared;

/**
 * Another showcase of sharing code between server and client. This data
 * validation code is written once but can be used both in client (JavaScript
 * code is generated by the GWT compiler) and in server (useful when the GWT UI
 * is not the only exposed interface of the application).
 * 
 * @author Milan
 * 
 */
public class SimpleNameValidator {

	/**
	 * Some validations done on name, if it's valid method returs true.
	 * 
	 * @param name
	 * @return
	 */
	public boolean validateName(String name) {
		// some funny rules
		if (name.length() > 2 && name.length() < 100 && !name.equals("Dracula")) {
			return true;
		}
		return false;
	}
}
