/*
 * GreyscaleFilter.java
 * Created on 15 August 2004, 14:45
 *
 * Copyright 2004, Generation5. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package com.itba.imagenes.hough;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * This class converts an RGB colour to a greyscale image.
 * 
 * @author James Matthews
 */
public class GreyscaleFilter extends Filter {
	/** BT709 Greyscale: Red: 0.2125 Green: 0.7154 Blue: 0.0721 */
	public static final int BT709 = 0;

	/** Y-Greyscale (YIQ/NTSC): Red: 0.299 Green: 0.587 Blue: 0.114 */
	public static final int Y = 1;

	/** RMY Greyscale: Red: 0.5 Green: 0.419 Blue: 0.081 */
	public static final int RMY = 2;

	/** The greyscale type (BT709, Y or RMY) */
	protected int greyscaleType = BT709;

	/**
	 * Creates a new instance of GreyscaleFilter
	 */
	public GreyscaleFilter() {
	}

	/**
	 * Convert an RGB image to greyscale.
	 * 
	 * @param image
	 *            the input image.
	 * @param output
	 *            the output image (optional).
	 * 
	 * @return the output image.
	 */
	@SuppressWarnings("static-access")
	public java.awt.image.BufferedImage filter(BufferedImage image,
			BufferedImage output) {
		// Verify our output variable
		output = verifyOutput(output, image, BufferedImage.TYPE_BYTE_GRAY);

		Raster in_pixels = image.getRaster();
		WritableRaster out_pixels = output.getRaster();

		if ((image.getType() == image.TYPE_BYTE_GRAY)
				|| (image.getType() == image.TYPE_USHORT_GRAY)) {
			return image;
		}

		int r;
		int g;
		int b;
		int gc;

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				r = in_pixels.getSample(i, j, 0);
				g = in_pixels.getSample(i, j, 1);
				b = in_pixels.getSample(i, j, 2);

				gc = calculateGrey(r, g, b);
				out_pixels.setSample(i, j, 0, gc);
			}
		}

		return output;
	}

	/**
	 * A static utility function to generate a greyscale image. Simply
	 * implemented as: <code> GreyscaleFilter grey = new GreyscaleFilter();
	 * return grey.filter(input, output); </code>
	 * 
	 * @param input
	 *            the input image.
	 * @param output
	 *            the output image (optional).
	 * 
	 * @return the greyscale image.
	 */
	public static BufferedImage toGrey(BufferedImage input, BufferedImage output) {
		GreyscaleFilter grey = new GreyscaleFilter();

		return grey.filter(input, output);
	}

	/**
	 * Calculate the grey value according to the grey conversion set.
	 * 
	 * @param r
	 *            the red value.
	 * @param g
	 *            the green value.
	 * @param b
	 *            the blue value.
	 * 
	 * @return the calculated grey value.
	 */
	protected int calculateGrey(int r, int g, int b) {
		switch (greyscaleType) {
		case BT709:
			return (int) ((0.2125 * r) + (0.7154 * g) + (0.0721 * b));

		case Y:
			return (int) ((0.299 * r) + (0.587 * g) + (0.114 * b));

		case RMY:
			return (int) ((0.5 * r) + (0.419 * g) + (0.081 * b));
		}

		return 255;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String toString() {
		return "Greyscale";
	}
}