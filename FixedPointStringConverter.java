package ch.epfl.alpano.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ch.epfl.alpano.Preconditions;
import javafx.util.StringConverter;

/**
 * double value string converter
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public class FixedPointStringConverter extends StringConverter<Integer> {
	// final?
	private final int decimals;

	/**
	 * construct a FixedPointStringConverter that uses decimals decimals double
	 * values
	 * 
	 * @param decimals
	 */
	public FixedPointStringConverter(int decimals) {
		Preconditions.checkArgument(decimals >= 0);
		this.decimals = decimals;
	}

	/**
	 * @return number of decimals used to convert
	 */
	public int decimals() {
		return decimals;
	}

	@Override
	public String toString(Integer value) {
		return new BigDecimal(value).movePointLeft(decimals).toPlainString();
	}

	@Override
	public Integer fromString(String string) {
		return new BigDecimal(string).movePointRight(decimals).setScale(0, RoundingMode.HALF_UP).intValueExact();
	}

}
