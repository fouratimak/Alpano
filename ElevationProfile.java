package ch.epfl.alpano.dem;

import java.util.ArrayList;
import java.util.Objects;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Preconditions;
import static java.lang.Math.asin;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.PI;
import static ch.epfl.alpano.Math2.PI2;

public final class ElevationProfile {
	private final ContinuousElevationModel elevationModel;
	private final GeoPoint origin;
	private final double azimuth;
	private final double length;
	private final ArrayList<GeoPoint> samples;
	private final static double SPACING = 4096;

	/**
	 * construct an elevation profile based on samples located on the great
	 * circle (corresponding to the origin and the azimuth) every SPACING meters
	 * 
	 * @param elevationModel
	 * @param origin
	 * @param azimuth
	 * @param length
	 * @throws IllegalArgumentException
	 */
	public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length) {

		Preconditions.checkArgument(Azimuth.isCanonical(azimuth) && length > 0);
		Objects.requireNonNull(elevationModel);
		Objects.requireNonNull(origin);

		this.elevationModel = elevationModel;
		this.origin = origin;
		this.azimuth = azimuth;
		this.length = length;

		samples = new ArrayList<>();

		double distance = 0.0;
		double direction = Azimuth.toMath(this.azimuth);

		for (int i = 0; i < (this.length / SPACING) + 1; ++i) {
		        distance=i*SPACING;
		        double angle=Distance.toRadians(distance);
			double latitude = asin(sin(this.origin.latitude()) * cos(angle)
					+ cos(this.origin.latitude()) * sin(angle) * cos(direction));

			double longitude =(this.origin.longitude() 
			        + PI- asin(sin(direction)
			        * sin(angle) / cos(latitude)))%PI2 - PI;

			samples.add(new GeoPoint(longitude, latitude));
		}

	}

	/** 
	 * @param x 
	 * @return the elevation of the point located at x meters from the origin
	 * @throws IllegalArgumentException
	 */
	public double elevationAt(double x) {
		Preconditions.checkArgument(x >= 0.0 && x <= length);
		return elevationModel.elevationAt(positionAt(x));
	}

	/**
	 * @param x
	 * @return the position of the point located at x meters from the origin
	 * @throws IllegalArgumentException
	 */
	public GeoPoint positionAt(double x) {
		Preconditions.checkArgument(x >= 0.0 && x <= length);
		double longitude = 0.0;
		double latitude = 0.0;
		int sample1 = (int) Math.floor(x / SPACING);
        int sample2 = sample1 + 1;

        longitude = Math2.lerp(samples.get(sample1).longitude(), samples.get(sample2).longitude(),
                x / SPACING - sample1);
        latitude = Math2.lerp(samples.get(sample1).latitude(), samples.get(sample2).latitude(), x / SPACING - sample1);

        return new GeoPoint(longitude, latitude);
	}

	/**
	 * @param x
	 * @return the slope of the point located at x meters from the origin
	 * @throws IllegalArgumentException
	 */
	public double slopeAt(double x) {
		Preconditions.checkArgument(x >= 0.0 && x <= length);
		return elevationModel.slopeAt(positionAt(x));
	}
}
