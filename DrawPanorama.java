/*
 *	Author:      Haitham Hammami
 *	Date:        22 Mar 2017
 */

package ch.epfl.alpano;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.gui.ChannelPainter;
import ch.epfl.alpano.gui.ImagePainter;
import ch.epfl.alpano.gui.Labelizer;
import ch.epfl.alpano.gui.PanoramaRenderer;
import ch.epfl.alpano.gui.PredefinedPanoramas;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.GazetteerParserTest;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

final class DrawPanorama {
    final static File HGT_FILE = new File("N46E007.hgt");

    final static int IMAGE_WIDTH = 500;
    final static int IMAGE_HEIGHT = 200;

    final static double ORIGIN_LON = toRadians(7.65);
    final static double ORIGIN_LAT = toRadians(46.73);
    final static int ELEVATION = 600;
    final static double CENTER_AZIMUTH = toRadians(180);
    final static double HORIZONTAL_FOV = toRadians(60);
    final static int MAX_DISTANCE = 100_000;

    final static PanoramaParameters PARAMS =PredefinedPanoramas.niesen.panoramaDisplayParameters();
    		
      /*new PanoramaParameters(new GeoPoint(ORIGIN_LON,
                                          ORIGIN_LAT),
                             ELEVATION,
                             CENTER_AZIMUTH,
                             HORIZONTAL_FOV,
                             MAX_DISTANCE,
                             IMAGE_WIDTH,
                             IMAGE_HEIGHT);*/

    public static void main(String[] as) throws Exception {
   
      try (DiscreteElevationModel dDEM =
           new HgtDiscreteElevationModel(HGT_FILE)) {
        ContinuousElevationModel cDEM =
          new ContinuousElevationModel(dDEM);
        Panorama p = new PanoramaComputer(cDEM)
          .computePanorama(PARAMS);

        Labelizer labelizer = new Labelizer(cDEM, GazetteerParser.readSummitsFrom(new File("alps.txt")));
        labelizer.labels(PARAMS).toString();
        System.out.println(labelizer.labels(PARAMS));
        BufferedImage i =
          new BufferedImage(IMAGE_WIDTH,
                            IMAGE_HEIGHT,
                            TYPE_INT_RGB);

        for (int x = 0; x < IMAGE_WIDTH; ++x) {
          for (int y = 0; y < IMAGE_HEIGHT; ++y) {
            float d = p.distanceAt(x, y);
            int c = (d == Float.POSITIVE_INFINITY)
              ? 0x87_CE_EB
              : gray((d - 2_000) / 15_000);
            i.setRGB(x, y, c);
          }
        }

        ImageIO.write(i, "png", new File("niesen.png"));
        ChannelPainter gray =
                ChannelPainter.maxDistanceToNeighbors(p)
                .sub(500)
                .div(4500)
                .clamped()
                .inverted();

              ChannelPainter distance = p::distanceAt;
              ChannelPainter slope = p::slopeAt;
              ChannelPainter opacity =
                distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
              ChannelPainter h=distance.div(100000f).cycle().mul(360f);
              ChannelPainter s=distance.div(200000f).clamped().inverted();
              ChannelPainter b=slope.mul(2f).div((float)Math.PI).inverted()
                                  .mul(0.7f).add(0.3f);
                      
            //  ImagePainter l = ImagePainter.gray(gray, opacity);
              ImagePainter l = ImagePainter.hsb(h, s, b, opacity);

              Image i2 = PanoramaRenderer.renderPanorama(p, l);
              ImageIO.write(SwingFXUtils.fromFXImage(i2, null),
                            "png",
                            new File("niesen-profile.png"));
      }
      
    }
    private static int gray(double v) {
        double clampedV = max(0, min(v, 1));
        int gray = (int) (255.9999 * clampedV);
        return (gray << 16) | (gray << 8) | gray;
      }
  }