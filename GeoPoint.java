/**
 *	Author:      Haitham Hammami
 *	Date:        27 Feb 2017
 */

package ch.epfl.alpano;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

import java.util.Locale;

/**
 * GeoPoint : points in geodetic system (admitting that the earth is a sphere)
 * 
 * @author Makki Fourati (247746)
 * @author Haitham Hammami (257479)
 */
public final class GeoPoint {
	private final double longitude;
	private final double latitude;

	/**
	 * constructs a geopoint with a longitude and a latitude
	 * 
	 * @param longitude
	 * @param latitude
	 * @throws IllegalArgumentException
	 */
	public GeoPoint(double longitude, double latitude) {
		Preconditions.checkArgument(longitude <= PI && longitude >= -PI);
		Preconditions.checkArgument(latitude <= PI / 2 && latitude >= -PI / 2);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * @return the longitude of the geopoint
	 */
	public double longitude() {
		return longitude;
	}

	/**
	 * @return the latitude of the geopoint
	 */
	public double latitude() {
		return latitude;
	}

	/**
	 * @param that
	 * @return the distance in meters between two geopoints
	 */
	public double distanceTo(GeoPoint that) {
		double x = Math2.haversin(this.latitude() - that.latitude());
		double y = cos(this.latitude()) * cos(that.latitude()) * Math2.haversin(this.longitude() - that.longitude());
		double a = 2 * Math.asin(Math.sqrt(x + y));
		return Distance.toMeters(a);
	}

	/**
	 * @param that
	 * @return the azimuth between two geopoints
	 */
	public double azimuthTo(GeoPoint that) {
		double a = (sin(this.longitude() - that.longitude()) * cos(that.latitude()));
		double b = (cos(this.latitude()) * sin(that.latitude())
				- cos(that.latitude()) * sin(this.latitude()) * cos(this.longitude() - that.longitude()));
		return Azimuth.fromMath(Azimuth.canonicalize(Math.atan2(a, b)));
	}

	@Override
	public String toString() {
		Locale l = null;
		double a = 180 * longitude() / Math.PI;
		double b = 180 * latitude() / Math.PI;
		String s = String.format(l, "(%.4f, %.4f)", a, b);
		return s;

	}
}
