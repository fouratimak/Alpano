
package ch.epfl.alpano;

import static java.lang.Math.PI;
import static ch.epfl.alpano.Math2.PI2;;

/**
 * Azimuth : horizontal angle between a referential vertical plane and the
 * vertical plane containing the object
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public interface Azimuth {
	/**
	 * @param azimuth
	 * @return true if the azimuth is canonical i.e between 0 and 2*PI
	 */
	public static boolean isCanonical(double azimuth) {
		return (azimuth >= 0 && azimuth < PI2);

	}

	/**
	 * @param azimuth
	 * @return a canonicalized azimuth
	 */
	public static double canonicalize(double azimuth) {
		while (!isCanonical(azimuth)) {
			if (azimuth >= PI2)
				azimuth -= PI2;
			else if (azimuth < 0)
				azimuth += PI2;

		}
		return azimuth;
	}

	/**
	 * @param azimuth
	 * @return the angle corresponding to the azimuth
	 * @throws IllegalArgumentException
	 */
	public static double toMath(double azimuth) {
		Preconditions.checkArgument(isCanonical(azimuth));
		return canonicalize(PI2 - azimuth);
	}

	/**
	 * @param angle
	 * @return the azimuth corresponding to the angle
	 * @throws IllegalArgumentException
	 */
	public static double fromMath(double angle) {
		Preconditions.checkArgument(isCanonical(angle));
		return canonicalize(PI2 - angle);
	}

	/**
	 * @param azimuth
	 * @param n
	 * @param e
	 * @param s
	 * @param w
	 * @return the octant corresponding to the azimuth
	 * @throws IllegalArgumentException
	 */
	public static String toOctantString(double azimuth, String n, String e, String s, String w) {
		Preconditions.checkArgument(isCanonical(azimuth));
		String oct = "";
		if (azimuth < 3 * PI / 8 || azimuth > 13 * PI / 8)
			oct += n;
		if (azimuth < 11 * PI / 8 && azimuth > 5 * PI / 8)
			oct += s;
		if (azimuth > PI / 8 && azimuth < 7 * PI / 8)
			oct += e;
		if (azimuth < 15 * PI / 8 && azimuth > 9 * PI / 8)
			oct += w;
		return oct;

	}
}
