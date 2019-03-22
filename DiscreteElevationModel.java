package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

/**
 * Discrete elevation model
 * 
 * @author Makki Fourati (247746)
 * @author Haitham Hammami (257479)
 */
public interface DiscreteElevationModel extends AutoCloseable {

	public static int SAMPLES_PER_DEGREE = 3600;
	public static double SAMPLES_PER_RADIAN = 180 * SAMPLES_PER_DEGREE / Math.PI;

	/**
	 * @param angle
	 * @return the sample index corresponding to the angle
	 */
	public static double sampleIndex(double angle) {
		return angle * SAMPLES_PER_RADIAN;
	}

	/**
	 * @return the extent of the interval
	 */
	public Interval2D extent();

	/**
	 * @param x
	 * @param y
	 * @return the elevation sample of the point (x,y)
	 */
	public double elevationSample(int x, int y);

	/**
	 * @param that
	 * @return a Composite DEM representing the union of the two DEM
	 * @throws IllegalArgumentException
	 */
	public default DiscreteElevationModel union(DiscreteElevationModel that) {
		Interval2D i1 = this.extent();
		Interval2D i2 = that.extent();
		Preconditions.checkArgument(i1.isUnionableWith(i2));
		return new CompositeDiscreteElevationModel(this, that);

	}

}
