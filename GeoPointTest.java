package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeoPointTest {

	private GeoPoint rolex = new GeoPoint(6.56728*Math.PI/180, 46.51780*Math.PI/180);
	private GeoPoint eugier = new GeoPoint(8.00543*Math.PI/180, 46.57758*Math.PI/180);
	
	@Test
	public void distanceToWorks() {
		double d = rolex.distanceTo(eugier);
		assertEquals(110490, d, 10);
	}
	
	@Test
	public void azimuthToWorks() {
		double a = rolex.azimuthTo(eugier);
		assertEquals(86.67, a, 10);
	}

}
