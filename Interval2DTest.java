package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class Interval2DTest {

	@Test(expected = NullPointerException.class)
	public void interval2DThrowsForNullArguments() {
		Interval2D iX = new Interval2D(null, new Interval1D(1, 2));
		Interval2D iY = new Interval2D(new Interval1D(1, 2), null);
	}

	@Test
	public void containsWorksOnTrivial() {
		Interval1D iX = new Interval1D(0, 0);
		Interval1D iY = new Interval1D(1, 1);
		Interval2D i = new Interval2D(iX, iY);
		assertEquals(true, i.contains(0, 1));
		assertEquals(false, i.contains(1, 0));
	}

	@Test
	public void containsWorksOnNonTrivial() {
		Interval1D iX = new Interval1D(2, 8);
		Interval1D iY = new Interval1D(4, 10);
		Interval2D i = new Interval2D(iX, iY);
		assertEquals(true, i.contains(4, 9));
		assertEquals(false, i.contains(10, 2));
	}

	@Test
	public void sizeWorksOnTrivial() {
		Interval1D iX = new Interval1D(0, 0);
		Interval1D iY = new Interval1D(1, 1);
		Interval2D i = new Interval2D(iX, iY);
		assertEquals(1, i.size(), 0);

	}

	@Test
	public void sizeWorksOnNonTrivial() {
		Interval1D iX = new Interval1D(2, 4);
		Interval1D iY = new Interval1D(3, 6);
		Interval2D i = new Interval2D(iX, iY);
		assertEquals(12, i.size(), 0);

	}

	@Test
	public void sizeOfIntersectionWithWorks() {
		Interval1D iX1 = new Interval1D(2, 6);
		Interval1D iY1 = new Interval1D(3, 7);
		Interval1D iX2 = new Interval1D(5, 12);
		Interval1D iY2 = new Interval1D(5, 12);

		Interval2D i1 = new Interval2D(iX1, iY1);
		Interval2D i2 = new Interval2D(iX2, iY2);

		assertEquals(6, i1.sizeOfIntersectionWith(i2));

	}

	@Test
	public void boundingUnionWorks() {
		Interval1D iX1 = new Interval1D(2, 6);
		Interval1D iY1 = new Interval1D(3, 7);
		Interval1D iX2 = new Interval1D(5, 12);
		Interval1D iY2 = new Interval1D(5, 12);

		Interval2D i1 = new Interval2D(iX1, iY1);
		Interval2D i2 = new Interval2D(iX2, iY2);

		assertEquals(new Interval2D(new Interval1D(2, 12), new Interval1D(3, 12)), i1.boundingUnion(i2));

	}

	@Test
	public void isUnionableWithWorks() {
		Interval1D iX1 = new Interval1D(0, 5);
		Interval1D iY1 = new Interval1D(4, 10);
		Interval1D iX2 = new Interval1D(1, 3);
		Interval1D iY2 = new Interval1D(2, 7);
		/*Interval1D iX1 = new Interval1D(2, 6);
		Interval1D iY1 = new Interval1D(3, 7);
		Interval1D iX2 = new Interval1D(6, 12);
		Interval1D iY2 = new Interval1D(5, 12);
		Interval1D iX3 = new Interval1D(-1, 0);
		Interval1D iY3 = new Interval1D(5, 5);
*/
		Interval2D i1 = new Interval2D(iX1, iY1);
		Interval2D i2 = new Interval2D(iX2, iY2);
	//	Interval2D i3 = new Interval2D(iX3, iY3);

		assertEquals(false, i1.isUnionableWith(i2));
	//	assertEquals(false, i3.isUnionableWith(i1));
	//	assertEquals(false, i3.isUnionableWith(i2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void unionThrowsException(){
		Interval1D iX1 = new Interval1D(0, 5);
		Interval1D iY1 = new Interval1D(4, 10);
		Interval1D iX2 = new Interval1D(1, 3);
		Interval1D iY2 = new Interval1D(2, 7);
		
		Interval2D i1 = new Interval2D(iX1, iY1);
		Interval2D i2 = new Interval2D(iX2, iY2);
		
		i1.union(i2);
	}
	
	@Test
	public void equalsWorks() {
		Interval1D i = new Interval1D(2, 6);
		Interval1D j = new Interval1D(2, 6);
		Interval1D k = new Interval1D(0, 6);
		
		assertEquals(false, i.equals(null));

		assertEquals(false, i.equals(k));
		assertEquals(true, i.equals(j));
	}

}
