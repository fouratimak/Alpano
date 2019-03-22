
package ch.epfl.alpano.dem;

import java.util.Objects;

import ch.epfl.alpano.Interval2D;

/**
 * Composite discrete elevation model
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
final class CompositeDiscreteElevationModel implements DiscreteElevationModel {
	private final DiscreteElevationModel dem1, dem2;

	/**
	 * constructs a composite DEM from two DEMs
	 * 
	 * @param dem1
	 * @param dem2
	 * @throws NullPointerException
	 *             if one of the DEMs is null
	 */
	CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2) {
		Objects.requireNonNull(dem1);
		Objects.requireNonNull(dem2);
		this.dem1 = dem1;
		this.dem2 = dem2;
	}

	@Override
	public Interval2D extent() {
		return dem1.extent().union(dem2.extent());
	}

	@Override
	public double elevationSample(int x, int y) {
		if (dem1.extent().contains(x, y))
			return dem1.elevationSample(x, y);
		else
			return dem2.elevationSample(x, y);
	}

	@Override
	public void close() throws Exception {
		dem1.close();
		dem2.close();

	}

}
