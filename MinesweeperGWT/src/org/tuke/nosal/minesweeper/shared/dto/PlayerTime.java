package org.tuke.nosal.minesweeper.shared.dto;

/**
 * Class for keeping best times of players. To sent it over the GWT RPC it would
 * be sufficient to implement IsSerializable or Serializable marker interfaces,
 * but then we would have to have a zero-arg constructor. Here that would not be
 * a problem, but we want to take a look at how to deal with non-standard
 * situations. For that we will need a CustomFieldSerializer.
 * 
 * @author Milan
 * 
 */
public class PlayerTime {
	/** Player name. It can be final because we use custom serializer. */
	private final String name;

	/** Playing time in seconds. */
	private final int time;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            player name
	 * @param time
	 *            playing game time in seconds
	 */
	public PlayerTime(String name, int time) {
		this.name = name;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public int getTime() {
		return time;
	}
}