
package ch.epfl.alpano;

/**
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public interface Distance {
	/**
	 * Earth radius in meters
	 */
	public static double EARTH_RADIUS = 6371000;

	/**
	 * @param distanceInMeters
	 * @return the distance in radians
	 */
	public static double toRadians(double distanceInMeters) {
		return distanceInMeters / EARTH_RADIUS;
	}

	/**
	 * @param distanceInRadians
	 * @return the distance in meters
	 */
	public static double toMeters(double distanceInRadians) {
		return distanceInRadians * EARTH_RADIUS;
	}
}
