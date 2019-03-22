package ch.epfl.alpano.gui;

import ch.epfl.alpano.Preconditions;
import javafx.util.StringConverter;

/**
 * Labeled list string converter
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public class LabeledListStringConverter extends StringConverter<Integer> {

	private String[] stringList;

	/**
	 * constructs a LabeledListStringConverter with a string list
	 * 
	 * @param stringList
	 */
	public LabeledListStringConverter(String... stringList) {
		this.stringList = stringList.clone();
	}

	/**
	 * @return string list
	 */
	public String[] stringList() {

		return stringList.clone();
	}

	@Override
	public String toString(Integer value) {
		Preconditions.checkArgument(value >= 0 && value < stringList().length);

		return stringList()[value];
	}

	@Override
	public Integer fromString(String string) {
		for (int i = 0; i < stringList().length; ++i) {
			if (string.equals(stringList()[i]))
				return i;
		}
		throw new IllegalArgumentException();
	}

}
