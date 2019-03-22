
package ch.epfl.alpano.dem;

import java.util.Objects;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.dem.DiscreteElevationModel;

/**
 * Continuous elevation model
 * 
 * @author Makki Fourati (247746)
 * @author Haitham Hammami (257479)
 */
public final class ContinuousElevationModel {
	private final DiscreteElevationModel dem;
	private final double NULL_ELEVATION = 0.0; // used for the dem's extension
	/**
	 * distance between two vertically successive samples
	 */
	private static double dNS = Distance.toMeters(1 / DiscreteElevationModel.SAMPLES_PER_RADIAN);

	/**
	 * construct a continuous elevation model from a discrete one
	 * 
	 * @param dem
	 * @throws NullPointerException
	 */
	public ContinuousElevationModel(DiscreteElevationModel dem) {
		Objects.requireNonNull(dem);
		this.dem = dem;
	}

	/**
	 * @param p
	 * @return the elevation of a point p using bilinear interpolation
	 */
	public double elevationAt(GeoPoint p) {

		double x = DiscreteElevationModel.sampleIndex(p.longitude());
		double y = DiscreteElevationModel.sampleIndex(p.latitude());

		// round up and down x and y
		int x0 = (int) Math.floor(x);
		int x1 = x0 + 1;
		int y0 = (int) Math.floor(y);
		int y1 = y0 + 1;

		double z00 = demExtensionElevation(x0, y0);
		double z10 = demExtensionElevation(x1, y0);
		double z01 = demExtensionElevation(x0, y1);
		double z11 = demExtensionElevation(x1, y1);

		return Math2.bilerp(z00, z10, z01, z11, x - x0, y - y0);
	}

	/**
	 * @param p
	 * @return the slope of a point p using bilinear interpolation
	 */
	public double slopeAt(GeoPoint p) {
		double x = DiscreteElevationModel.sampleIndex(p.longitude());
		double y = DiscreteElevationModel.sampleIndex(p.latitude());

		// round up and down x and y
		int x0 = (int) Math.floor(x);
		int x1 = x0 + 1;
		int y0 = (int) Math.floor(y);
		int y1 = y0 + 1;

		double teta00 = demExtensionSlope(x0, y0);
		double teta10 = demExtensionSlope(x0, y1);
		double teta01 = demExtensionSlope(x1, y0);
		double teta11 = demExtensionSlope(x1, y1);

		return Math2.bilerp(teta00, teta01, teta10, teta11, x - x0, y - y0);
	}

	/**
	 * @param x
	 * @param y
	 * @return the elevation of the sample if it is contained in the DEM , 0
	 *         otherwise
	 */
	private double demExtensionElevation(int x, int y) {
		if (dem.extent().contains(x, y)) {
			return dem.elevationSample(x, y);
		} else {
			return NULL_ELEVATION;
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return the slope of sample using two close samples
	 */
	private double demExtensionSlope(int x, int y) {

		double z = demExtensionElevation(x, y);
		double zA = demExtensionElevation(x + 1, y);
		double zB = demExtensionElevation(x, y + 1);
		double deltaZA = zA - z;
		double deltaZB = zB - z;

		return Math.acos(dNS / (Math.sqrt(Math2.sq(deltaZA) + Math2.sq(deltaZB) + Math2.sq(dNS))));

	}

}
