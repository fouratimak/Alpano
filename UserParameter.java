/*
 *	Author:      Haitham Hammami
 *	Date:        25 Apr 2017
 */

package ch.epfl.alpano.gui;

/**
 * Enumeration of the limit values of the panorama parameters
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public enum UserParameter {
	OBSERVER_LONGITUDE(60000, 120000), OBSERVER_LATITUDE(450000, 480000), OBSERVER_ELEVATION(300,
			10000), CENTER_AZIMUTH(0, 359), HORIZONTAL_FIELD_OF_VIEW(1,
					360), MAX_DISTANCE(10, 600), WIDTH(30, 16000), HEIGHT(10, 4000), SUPER_SAMPLING_EXPONENT(0, 2);

	private int min, max;

	private UserParameter(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * @param value
	 * @return the parameter value after being sanitized using the corresponding
	 *         limit values
	 */
	public int sanitize(int value) {
		if (this.min > value)
			value = this.min;
		else if (this.max < value)
			value = this.max;
		return value;
	}
}
