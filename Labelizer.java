
package ch.epfl.alpano.gui;

import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;
import static java.lang.Math.abs;
import java.util.Comparator;
import java.net.URI;
import static java.lang.Math.round;
import static java.lang.Math.atan2;
import ch.epfl.alpano.GeoPoint;
import static ch.epfl.alpano.Math2.*;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.shape.Line;
import static ch.epfl.alpano.PanoramaComputer.rayToGroundDistance;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
/**
 * Creating panorama summit labels
 *  @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public final class Labelizer {
    private final ContinuousElevationModel cem;
    private final List<Summit> summits;
    private static final int SPACING = 64;
    private static final int TOLERANCE = 200;
    private static final int SEPARATION = 20;
    private static final int MAX_Y = 170;
    private static final int ALIGNMENT=22;
    private static final int   ROTATION=-60;
    private static final int   Y_ALIGNMENT=6;
    private static final int   X_ALIGNMENT=18;
    
    
    /**constructs a labelizer using a cem and summit's list
     * @param cem
     * @param summits
     */
    public Labelizer(ContinuousElevationModel cem, List<Summit> summits) {
        this.cem = Objects.requireNonNull(cem);
        this.summits = Objects.requireNonNull(summits);

    }
    
    /**
     * @param parameters
     * @return a nodes list containing a text and a line for each labeled summit
     */
    public List<Node> labels(PanoramaParameters parameters) {
        List<Node> nodes = new LinkedList<>();
        List<CoordinateSummit> visibleSummits = visible(parameters);
       
        Comparator<CoordinateSummit> yComparator = (s1, s2) -> Double
                .compare(round(s1.getY()), round(s2.getY()));

        Comparator<CoordinateSummit> elevationComparator = (s1, s2) -> Double
                .compare(s2.getSummit().elevation(),
                        s1.getSummit().elevation());
        
        // sorting the visible summit list by the lowest y in the picture than by the greatest elevation
        Collections.sort(visibleSummits,
                yComparator.thenComparing(elevationComparator));

        for (int i = 0; i < visibleSummits.size(); ++i) {
            double y = visibleSummits.get(i).getY();
            if (y < MAX_Y) {
                visibleSummits.remove(visibleSummits.get(i));
            }
        }
        
        if (!visibleSummits.isEmpty()) {
            double maxElevation = visibleSummits.get(0).getY();
            int yMax = (int) round(maxElevation); //the highest summit position in the picture
            int yLabels = yMax - ALIGNMENT; //where all the labels text must be aligned
            BitSet labeled = new BitSet(parameters.width());

            for (CoordinateSummit s : visibleSummits) {
                int roundedX = (int) round(s.getX());

                int roundedY = (int) round(s.getY());

                if (roundedX > SEPARATION
                        && roundedX < parameters.width() - SEPARATION) {
                    BitSet next = labeled.get(roundedX, roundedX + SEPARATION);
                    if (next.cardinality() == 0) {
                        Hyperlink t = new Hyperlink(s.getSummit().name() + " ("
                                + s.getSummit().elevation() + " m)");
                        t.getTransforms().addAll(
                                new Translate(roundedX - X_ALIGNMENT, yLabels - Y_ALIGNMENT),
                                new Rotate(ROTATION, 0, 0));
                        t.setOnAction(event -> {
                            try {
                                URI osmURI = new URI("http", "www.google.ch",
                                        "/", "", "q=" + s.getSummit().name());
                                java.awt.Desktop.getDesktop().browse(osmURI);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        nodes.add(t);
                        Line line = new Line(roundedX, yLabels, roundedX,
                                roundedY);
                        nodes.add(line);

                        labeled.set(roundedX, roundedX + SEPARATION);
                    }
                }

            }
        }
        return nodes;
    }

    /**
     * @param parameters
     * @return the list of the visible summits before the labeling
     */
    private List<CoordinateSummit> visible(PanoramaParameters parameters) {
        List<CoordinateSummit> visibleSummits = new LinkedList<>();
        GeoPoint observerPosition = parameters.observerPosition();

        for (Summit s : summits) {

            double angle = observerPosition.azimuthTo(s.position());
            double dx = observerPosition.distanceTo(s.position());

            if ((abs(angularDistance(parameters.centerAzimuth(),
                    angle)) <= (parameters.horizontalFieldOfView() / 2d))
                    && (dx <= parameters.maxDistance())) {

                ElevationProfile profile = new ElevationProfile(cem,
                        observerPosition, angle, dx);
                DoubleUnaryOperator f1 = rayToGroundDistance(
                        profile, parameters.observerElevation(), 0d);
                double dy = -f1.applyAsDouble(dx);
                double altitude = atan2(dy, dx);
                if (abs(altitude) <= (parameters.verticalFieldOfView() / 2d)) {
                    DoubleUnaryOperator f2 =rayToGroundDistance(profile,
                       parameters.observerElevation(),dy/dx);

                    double distance = firstIntervalContainingRoot(f2, 0,
                            dx, SPACING);
                    if (distance >= dx - TOLERANCE)
                        visibleSummits.add(new CoordinateSummit(s,
                                parameters.xForAzimuth(angle),
                                parameters.yForAltitude(altitude)));

                }
            }

        }

        return visibleSummits;
    }
   /**
 * Coordinate Summit 
 *
 */
private static class CoordinateSummit {
        private double y;
        private double x;
        private Summit summit;

        /**
         * @param summit
         * @param x
         * @param y
         * creates a summit associated with its x and y
         */
        private CoordinateSummit(Summit summit, double x, double y) {
            this.x = x;
            this.y = y;
            this.summit = summit;
        }

        /**
         * @return the summit
         */
        private Summit getSummit() {
            return summit;
        }

        /**
         * @return the y coordiante of the summit
         */
        private double getY() {
            return y;
        }

        /**
         * @return the x coordiante of the summit
         */
        private double getX() {
            return x;
        }
    }

}
