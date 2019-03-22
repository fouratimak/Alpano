/*
 *	Author:      Haitham Hammami
 *	Date:        10 Apr 2017
 */

package ch.epfl.alpano.gui;

import javafx.scene.paint.Color;
/**
 * Image painter
 *  @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public interface ImagePainter {

	/**
	 * @param x
	 * @param y
	 * @return the color of the pixel located at (x,y)
	 */
	Color colorAt(int x, int y);

	/**
	 * @param t
	 * @param s
	 * @param l
	 * @param o
	 * @return an image painter using 4 channels corresponding to the hsb
	 *         representation
	 */
	static ImagePainter hsb(ChannelPainter t, ChannelPainter s, ChannelPainter l, ChannelPainter o) {
		return (x, y) -> {
			return Color.hsb((double) t.valueAt(x, y), (double) s.valueAt(x, y), (double) l.valueAt(x, y),
					(double) o.valueAt(x, y));

		};
	}

	/**
	 * @param g
	 * @param o
	 * @return a gray image painter using 2 channels
	 */
	static ImagePainter gray(ChannelPainter g, ChannelPainter o) {
		return (x, y) -> {
			return Color.gray((double) g.valueAt(x, y), (double) o.valueAt(x, y));
		};
	}
}
