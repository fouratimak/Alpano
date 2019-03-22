
package ch.epfl.alpano;

import static java.lang.Math.PI;
import java.util.function.*;

/**
 * Mathematics numeric operations
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public interface Math2 {
	public static double PI2 = 2 * PI;

	/**
	 * @param x
	 * @return the square of x
	 */
	public static double sq(double x) {
		return x * x;
	}

	/**
	 * @param x
	 * @param y
	 * @return the floorMod of x over y
	 */
	public static double floorMod(double x, double y) {
		return x - y * Math.floor(x / y);
	}

	/**
	 * @param x
	 * @return the haversin of x
	 */
	public static double haversin(double x) {
		return Math.pow(Math.sin(x / 2), 2);
	}

	/**
	 * @param a1
	 * @param a2
	 * @return the angularDistance between a1 and a2
	 */
	public static double angularDistance(double a1, double a2) {
		return floorMod((a2 - a1 + PI), PI2) - PI;
	}

	/**
	 * @param y0
	 * @param y1
	 * @param x
	 * @return the linear interpolation of x between y0 and y1
	 */
	public static double lerp(double y0, double y1, double x) {
		return x * (y1 - y0) + y0;
	}

	/**
	 * @param z00
	 * @param z10
	 * @param z01
	 * @param z11
	 * @param x
	 * @param y
	 * @return the bilinear interpolation
	 */
	public static double bilerp(double z00, double z10, double z01, double z11, double x, double y) {
		double z1 = lerp(z00, z10, x);
		double z2 = lerp(z01, z11, x);
		return lerp(z1, z2, y);
	}

	/**
	 * @param f
	 * @param minX
	 * @param maxX
	 * @param dX
	 * @return the first interval containing a root in f
	 */
	public static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX) {
		for (double i = minX; i < maxX; i += dX) {
			double fOfX1 = f.applyAsDouble(i);
			double fOfX2;
			if ((i + dX) > maxX)
				fOfX2 = f.applyAsDouble(maxX);
			else
				fOfX2 = f.applyAsDouble(i + dX);

			if (fOfX1 * fOfX2 < 0)
				return i;
		}
		return Double.POSITIVE_INFINITY;
	}

	/**
	 * @param f
	 * @param x1
	 * @param x2
	 * @param epsilon
	 * @return a rough estimation of the abscissa of the root
	 * @throws IllegalArgumentException
	 */
	public static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon) {
		double fx1 = f.applyAsDouble(x1);
		double fx2 = f.applyAsDouble(x2);
		Preconditions.checkArgument(fx1 * fx2 <= 0);
		while (Math.abs(x2 - x1) > epsilon) {
			double xm = (x1 + x2) / 2;
			double fxm = f.applyAsDouble(xm);
			if (fxm == 0)
				return xm;
			else if (fxm * fx1 < 0)
				x2 = xm;
			else
				x1 = xm;
		}
		return x1;
	}
}
