/*
 *	Author:      Haitham Hammami
 *	Date:        22 Mar 2017
 */

package ch.epfl.alpano;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.Panorama.Builder;
import ch.epfl.alpano.PanoramaParameters;;

public final class PanoramaComputer {
    private final ContinuousElevationModel dem;
    private static final double K = 0.13;

    public PanoramaComputer(ContinuousElevationModel dem) {
        Objects.requireNonNull(dem);
        this.dem = dem;
    }

    public Panorama computePanorama(PanoramaParameters parameters) {
        Builder b = new Builder(parameters);

        for (int i = 0; i < parameters.width(); i++) {
            ElevationProfile p = new ElevationProfile(dem,
                    parameters.observerPosition(), parameters.azimuthForX(i),
                    parameters.maxDistance());
            double root = 0;
            System.out.println(i);
            for (int j = parameters.height() - 1; j >= 0; j--) {

                double slope = parameters.altitudeForY(j);
                DoubleUnaryOperator f = rayToGroundDistance(p,
                        parameters.observerElevation(), Math.tan(slope));
                double bound = Math2.firstIntervalContainingRoot(f, root,
                        parameters.maxDistance(), 64);
                if (bound != Double.POSITIVE_INFINITY) {
                    root = Math2.improveRoot(f, bound, bound + 64, 4);
                    b.setDistanceAt(i, j, (float) (root/Math.cos(slope)));
                    b.setElevationAt(i, j, (float) dem.elevationAt(p.positionAt(root)));
                    b.setLatitudeAt(i, j,
                            (float) p.positionAt(root).latitude());
                    b.setLongitudeAt(i, j,
                            (float) p.positionAt(root).longitude());
                    b.setSlopeAt(i, j, (float) dem.slopeAt(p.positionAt(root)));
                }
            }

        }

        return b.build();
    }

    public static DoubleUnaryOperator rayToGroundDistance(
            ElevationProfile profile, double ray0, double raySlope) {
        return (x) -> ray0 + x * (raySlope) - profile.elevationAt(x)
                + x * x * (1d - K) / (2 * Distance.EARTH_RADIUS);
    }
}
