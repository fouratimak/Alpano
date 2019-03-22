/*
 *	Author:      Haitham Hammami
 *	Date:        25 Apr 2017
 */

package ch.epfl.alpano.gui;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import static java.lang.Math.*;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;
/**
 * Parameters introduced by the user 
 *  @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public final class PanoramaUserParameters {

	private final Map<UserParameter, Integer> parameters;
	private final int MAX_VERTICAL = 170;
	private final double SCALE = 10000;
	private final int TO_METERS = 1000;

	/**
	 * construct a PanoramaUserParameters using a map of parameters that are
	 * sanitized using their limit values from the key map
	 * 
	 * @param parameters
	 */
	public PanoramaUserParameters(Map<UserParameter, Integer> parameters) {

		int maxHeight = (MAX_VERTICAL * (parameters.get(UserParameter.WIDTH) - 1)
				/ parameters.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW)) + 1;
		if (maxHeight < parameters.get(UserParameter.HEIGHT))
			parameters.put(UserParameter.HEIGHT, maxHeight);
		for (UserParameter p : parameters.keySet())
			parameters.put(p, p.sanitize(parameters.get(p)));

		this.parameters = Collections.unmodifiableMap(new EnumMap<>(parameters));
	}

	/**
	 * construct a PanoramaUserParameters using the first constructor with the
	 * following parameters
	 * 
	 * @param longitude
	 * @param latitude
	 * @param elevation
	 * @param azimuth
	 * @param horizontal
	 * @param distance
	 * @param width
	 * @param height
	 * @param sampling
	 */
	public PanoramaUserParameters(int longitude, int latitude, int elevation, int azimuth, int horizontal, int distance,
			int width, int height, int sampling) {

		this(fill(longitude, latitude, elevation, azimuth, horizontal, distance, width, height, sampling));
	}

	private static Map<UserParameter, Integer> fill(int longitude, int latitude, int elevation, int azimuth,
			int horizontal, int distance, int width, int height, int sampling) {
		Map<UserParameter, Integer> parameters = new EnumMap<>(UserParameter.class);
		parameters.put(UserParameter.OBSERVER_LONGITUDE, longitude);
		parameters.put(UserParameter.OBSERVER_LATITUDE, latitude);
		parameters.put(UserParameter.OBSERVER_ELEVATION, elevation);
		parameters.put(UserParameter.CENTER_AZIMUTH, azimuth);
		parameters.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, horizontal);
		parameters.put(UserParameter.MAX_DISTANCE, distance);
		parameters.put(UserParameter.WIDTH, width);
		parameters.put(UserParameter.HEIGHT, height);
		parameters.put(UserParameter.SUPER_SAMPLING_EXPONENT, sampling);
		return parameters;

	}

	/**
	 * @param parameter
	 * @return the parameter corresponding to the given enumeration parameter
	 */
	public int get(UserParameter parameter) {
		return parameters.get(parameter);
	}

	/**
	 * @return the observer longitude
	 */
	public int observerLongitude() {
		return get(UserParameter.OBSERVER_LONGITUDE);
	}

	/**
	 * @return the observer latitude
	 */
	public int observerLatitude() {
		return get(UserParameter.OBSERVER_LATITUDE);
	}

	/**
	 * @return the observer elevation
	 */
	public int observerElevation() {
		return get(UserParameter.OBSERVER_ELEVATION);
	}

	/**
	 * @return the center azimuth
	 */
	public int centerAzimuth() {
		return get(UserParameter.CENTER_AZIMUTH);
	}

	/**
	 * @return the horizontal field of view
	 */
	public int horizontalFieldOfView() {
		return get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
	}

	/**
	 * @return the maximal distance
	 */
	public int maxDistance() {
		return get(UserParameter.MAX_DISTANCE);
	}

	/**
	 * @return the width of the image
	 */
	public int width() {
		return get(UserParameter.WIDTH);
	}

	/**
	 * @return the height of the image
	 */
	public int height() {
		return get(UserParameter.HEIGHT);
	}

	/**
	 * @return the super sampling exponent
	 */
	public int superSamplingExponent() {
		return get(UserParameter.SUPER_SAMPLING_EXPONENT);
	}

	/**
	 * @return a PanoramaParameters using the user parameters after converting
	 *         them to the appropriate values and considering the super sampling
	 *         exponent
	 */
	public PanoramaParameters panoramaParameters() {
		return new PanoramaParameters(
				new GeoPoint(toRadians(observerLongitude() / SCALE), toRadians(observerLatitude() / SCALE)),
				observerElevation(), Azimuth.canonicalize(toRadians(centerAzimuth())),
				toRadians(horizontalFieldOfView()), TO_METERS * maxDistance(),
				width() * (int) Math.pow(2, superSamplingExponent()),
				height() * (int) Math.pow(2, superSamplingExponent()));
	}

	/**
	 * @return a PanoramaParameters using the user parameters after converting
	 *         them to the appropriate values and without considering the super
	 *         sampling exponent
	 */
	public PanoramaParameters panoramaDisplayParameters() {
		return new PanoramaParameters(
				new GeoPoint(toRadians(observerLongitude() / SCALE), toRadians(observerLatitude() / SCALE)),
				observerElevation(), Azimuth.canonicalize(toRadians(centerAzimuth())),
				toRadians(horizontalFieldOfView()), TO_METERS * maxDistance(), width(), height());
	}

	@Override
	public boolean equals(Object that) {
		if (!(that instanceof PanoramaUserParameters) || that.equals(null))
			return false;
		return ((PanoramaUserParameters) that).parameters.equals(parameters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(parameters);
	}

}
