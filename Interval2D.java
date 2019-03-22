
package ch.epfl.alpano;

import java.util.Objects;

/**
 * Bidimensional discrete interval
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public final class Interval2D {
	private final Interval1D iX;
	private final Interval1D iY;

	/**
	 * constructs a bidimensional Interval using two unidimensional intervals
	 * 
	 * @param iX
	 * @param iY
	 * @throws NullPointerException
	 */
	public Interval2D(Interval1D iX, Interval1D iY) {

		Objects.requireNonNull(iX);
		Objects.requireNonNull(iY);
		this.iX = iX;
		this.iY = iY;

	}

	/**
	 * @return the interval of the abscissa
	 */
	public Interval1D iX() {
		return iX;
	}

	/**
	 * @return the interval of the ordinate
	 */
	public Interval1D iY() {
		return iY;
	}

	/**
	 * @param x
	 * @param y
	 * @return true if the interval contains the point (x,y)
	 */
	public boolean contains(int x, int y) {
		return (this.iX().contains(x) && this.iY().contains(y));
	}

	/**
	 * @return the size of the interval
	 */
	public int size() {
		return this.iX().size() * this.iY().size();
	}

	/**
	 * @param that
	 * @return the size of intersection between the two intervals
	 */
	public int sizeOfIntersectionWith(Interval2D that) {
		return this.iX().sizeOfIntersectionWith(that.iX()) * this.iY().sizeOfIntersectionWith(that.iY());
	}

	/**
	 * @param that
	 * @return the bounding union between two points
	 */
	public Interval2D boundingUnion(Interval2D that) {
		return new Interval2D(this.iX().boundingUnion(that.iX()), this.iY().boundingUnion(that.iY()));
	}

	/**
	 * @param that
	 * @return true if the intervals are unionable
	 */
	public boolean isUnionableWith(Interval2D that) {
		return ((this.size() + that.size() - this.sizeOfIntersectionWith(that)) == this.boundingUnion(that).size());
	}

	/**
	 * @param that
	 * @return the union of the two intervals
	 * @throws IllegalArgumentException
	 */
	public Interval2D union(Interval2D that) {
		Preconditions.checkArgument((this.isUnionableWith(that)));
		return this.boundingUnion(that);
	}

	@Override
	public boolean equals(Object thatO) {
		if (thatO == null) {
			return false;
		} else if (getClass() != thatO.getClass()) {
			return false;
		} else {
			return (((Interval2D) thatO).iX().equals(this.iX()) && ((Interval2D) thatO).iY().equals(this.iY()));
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.iX(), this.iY());
	}

	@Override
	public String toString() {
		return this.iX().toString() + 'x' + this.iY().toString();
	}
}
