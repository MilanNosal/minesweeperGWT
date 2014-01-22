package org.tuke.nosal.minesweeper.shared.dto;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class PlayerTime_CustomFieldSerializer {

	/**
	 * Mandatory method that serializes the instance of PlayerTime
	 * into SerializationStreamWriter ssw.
	 * @param ssw
	 * @param instance
	 * @throws SerializationException
	 */
	public static void serialize(SerializationStreamWriter ssw,
			PlayerTime instance) throws SerializationException {
		ssw.writeString(instance.getName());
		ssw.writeInt(instance.getTime());
	}

	/**
	 * Optional method that is needed when a zero-arg constructor
	 * is not present. We will use this.
	 * 
	 * @param ssr
	 * @return
	 * @throws SerializationException
	 */
	public static PlayerTime instantiate(SerializationStreamReader ssr)
			throws SerializationException {
		String name = ssr.readString();
		int time = ssr.readInt();
		return new PlayerTime(name, time);
	}

	/**
	 * Mandatory method for deserializing an instance. In our case
	 * the instantiate method did all the work.
	 * @param ssr
	 * @param instance
	 * @throws SerializationException
	 */
	public static void deserialize(SerializationStreamReader ssr,
			PlayerTime instance) throws SerializationException {
		// We have already done everything important in instantiate method
	}
}
