package ch.epfl.alpano;

import java.util.Objects;

/**
 * Unidimensional discrete interval
 * 
 * @author Makki (247746)
 * @author Haitham Hammami (257479)
 */

public final class Interval1D {

	private final int includedFrom;
	private final int includedTo;

	/**
	 * construct an integer interval starting at includedFrom and finishing at
	 * includedTo
	 * 
	 * @param includedFrom
	 * @param includedTo
	 * @throws IllegalArgumentException
	 */
	public Interval1D(int includedFrom, int includedTo) {
		Preconditions.checkArgument(includedFrom <= includedTo);

		this.includedFrom = includedFrom;
		this.includedTo = includedTo;

	}

	/**
	 * @return the lower bound of the interval
	 */
	public int includedFrom() {
		return includedFrom;
	}

	/**
	 * @return the upper bound of the interval
	 */
	public int includedTo() {
		return includedTo;
	}

	/**
	 * @param v
	 * @return true if v is included in the interval, false otherwise
	 */
	public boolean contains(int v) {
		return (v >= this.includedFrom() && v <= this.includedTo());
	}

	/**
	 * @return the size of the interval
	 */
	public int size() {
		return this.includedTo() - this.includedFrom() + 1;
	}

	/**
	 * @param that
	 * @return the number of element in the intersection
	 */
	public int sizeOfIntersectionWith(Interval1D that) {
		if ((this.includedTo() < that.includedFrom()) || (this.includedFrom() > that.includedTo())) {
			return 0;
		} else {
			int min = 0;
			int max = 0;

			if (this.includedFrom() <= that.includedFrom()) {
				min = that.includedFrom();
			} else {
				min = this.includedFrom();
			}

			if (this.includedTo() <= that.includedTo()) {
				max = this.includedTo();
			} else {
				max = that.includedTo();
			}
			return max - min + 1;
		}
	}

	/**
	 * @param that
	 * @return the bounding union of this and that
	 */
	public Interval1D boundingUnion(Interval1D that) {
		int min = 0;
		int max = 0;

		if (this.includedFrom() <= that.includedFrom()) {
			min = this.includedFrom();
		} else {
			min = that.includedFrom();
		}

		if (this.includedTo() <= that.includedTo()) {
			max = that.includedTo();
		} else {
			max = this.includedTo();
		}
		return new Interval1D(min, max);
	}

	/**
	 * @param that
	 * @return true if this is unionable with that, false otherwise
	 */
	public boolean isUnionableWith(Interval1D that) {
		return ((this.size() + that.size() - this.sizeOfIntersectionWith(that)) == this.boundingUnion(that).size());
	}

	/**
	 * @param that
	 * @return the union of this and that if they are unionable
	 * @throws IllegalArgumentException
	 */
	public Interval1D union(Interval1D that) {
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
			return ((this.includedFrom() == ((Interval1D) thatO).includedFrom())
					&& (this.includedTo() == ((Interval1D) thatO).includedTo()));
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(includedFrom(), includedTo());
	}

	@Override
	public String toString() {
		return "[" + this.includedFrom() + ".." + this.includedTo() + "]";
	}

}
